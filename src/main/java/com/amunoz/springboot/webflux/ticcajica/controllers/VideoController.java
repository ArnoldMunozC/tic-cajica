package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import com.amunoz.springboot.webflux.ticcajica.services.impl.VideoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    //private final VideoService videoService;
    private final VideoServiceImpl videoServiceImpl;
    private final CursoService cursoService;

    public VideoController( VideoServiceImpl videoServiceImpl, CursoService cursoService) {
        //this.videoService = videoService;
        this.videoServiceImpl = videoServiceImpl;
        this.cursoService = cursoService;
    }

    private final String videoDirectory = "./Users/arnmunoz/aprendizaje/videos";

    @PostMapping(value = "/create-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadVideos(
            @RequestPart("video") List<Video> videos,
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam("cursoId") UUID cursoId) {

        try {
            // Define la carpeta donde se almacenarán los vídeos
            Path root = Paths.get("C:\\Users\\Arnold Mauricio\\Documents\\videos");
            if (!Files.exists(root)) {
                Files.createDirectories(root); // Si el directorio no existe, crearlo
            }

            // Verifica que la cantidad de archivos y cursos coincida
            if (videos.size() != files.size()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Mismo número de archivos y cursos
            }

            // Itera sobre la lista de cursos y archivos
            for (int i = 0; i < videos.size(); i++) {
                Video currentCourse = videos.get(i);
                MultipartFile currentFile = files.get(i);

                // Nombre del archivo original
                String originalFilename = currentFile.getOriginalFilename();

                // Crear la ruta completa del archivo
                Path fileStorageLocation = root.resolve(originalFilename);

                // Almacena el archivo en el sistema de archivos local
                Files.write(fileStorageLocation, currentFile.getBytes());

                // Asigna la ruta del video al curso
                currentCourse.setVideoPath(fileStorageLocation.toString());

                // Guarda la entidad en la base de datos
                currentCourse.setCursoId(cursoId);
                videoServiceImpl.save(currentCourse);
            }

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Obtiene todos los cursos
     *
     * @return Lista de cursos
     */

    @GetMapping("/get-video/{id}")
    public Video getCourse(@PathVariable Long id) {
        Video video = videoServiceImpl.findById(id);
        return video;
    }


    @DeleteMapping("/delete-video/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        // Buscar el curso por su ID
        Video video = videoServiceImpl.findById(id);

        if (video != null) {
            try {
                // Obtener la ruta del video
                String videoPath = video.getVideoPath();

                // Si la ruta del video no es nula o vacía, intenta eliminar el archivo
                if (videoPath != null && !videoPath.isEmpty()) {
                    Path filePath = Paths.get(videoPath);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);  // Eliminar el archivo del sistema de archivos
                    }
                }

                // Eliminar el curso de la base de datos
                videoServiceImpl.delete(id);

                return new ResponseEntity<>(HttpStatus.OK); // Retornar 200 si fue exitoso
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Retornar 500 en caso de error
            }
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retornar 204 si no se encuentra el curso
    }


    @PutMapping(value = "/update-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateCourse(
            @RequestPart("video") Video video,
            @RequestParam("file") MultipartFile file) {

        try {
            // Obtener el ID del curso desde el cuerpo (objeto course)
            Long id = video.getId();

            // Buscar el curso actual por su ID
            Video currentCourse = videoServiceImpl.findById(id);

            // Si el curso no existe, retornar 404
            if (currentCourse == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Si se sube un archivo nuevo, se actualiza
            if (!file.isEmpty()) {
                // Eliminar el archivo de video anterior, si existe
                String oldVideoPath = currentCourse.getVideoPath();
                if (oldVideoPath != null && !oldVideoPath.isEmpty()) {
                    Path oldFilePath = Paths.get(oldVideoPath);
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);  // Eliminar el archivo anterior
                    }
                }

                // Definir la carpeta donde se almacenará el nuevo vídeo
                Path root = Paths.get("C:\\Users\\Arnold Mauricio\\Documents\\videos");
                if (!Files.exists(root)) {
                    Files.createDirectories(root); // Crear el directorio si no existe
                }

                // Obtener el nombre del archivo original
                String originalFilename = file.getOriginalFilename();

                // Crear la ruta completa donde se guardará el archivo
                Path fileStorageLocation = root.resolve(originalFilename);

                // Almacenar el archivo en el sistema de archivos
                Files.write(fileStorageLocation, file.getBytes());

                // Actualizar la ruta del nuevo video en el curso
                currentCourse.setVideoPath(fileStorageLocation.toString());
            }

            // Actualizar los otros campos del curso (si es necesario)
            currentCourse.setTitle(video.getTitle());
            // Puedes agregar más campos que quieras actualizar aquí

            // Guardar la actualización del curso en la base de datos
            videoServiceImpl.update(currentCourse, currentCourse.getId());

            return new ResponseEntity<>(HttpStatus.OK); // Retornar 200 si la actualización fue exitosa

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // retorna el video
    @GetMapping("/get-video-by-id/{id}")
    public void getVideoById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        // Buscar el video en la base de datos por su ID
        Video video = videoServiceImpl.findById(id);

        if (video == null) {
            // Retornar 404 si el video no existe
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        // Obtener la ruta del video desde la base de datos
        String videoPath = video.getVideoPath();

        // Verificar si el archivo de video existe en el sistema de archivos
        File videoFile = new File(videoPath);
        if (!videoFile.exists()) {
            // Retornar 404 si el archivo no existe
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        // Manejar los rangos solicitados por el cliente
        try (RandomAccessFile randomFile = new RandomAccessFile(videoFile, "r")) {
            // Obtener la longitud total del archivo
            long fileLength = randomFile.length();
            String range = request.getHeader("Range");

            long start = 0;
            long end = fileLength - 1;

            // Validar si el cliente solicitó un rango específico
            if (range != null && range.startsWith("bytes=")) {
                String[] ranges = range.substring(6).split("-");
                try {
                    start = Long.parseLong(ranges[0]); // Inicio del rango
                    if (ranges.length > 1) {
                        end = Long.parseLong(ranges[1]); // Fin del rango (opcional)
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return;
                }
            }

            // Validar que los rangos sean consistentes
            if (start > end || start >= fileLength) {
                response.setStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
                response.setHeader("Content-Range", "bytes */" + fileLength);
                return;
            }

            // Calcular la longitud del contenido solicitado
            long contentLength = end - start + 1;

            // Establecer los encabezados de la respuesta
            response.setStatus(range != null ? HttpStatus.PARTIAL_CONTENT.value() : HttpStatus.OK.value());
            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(contentLength));
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);

            // Enviar el contenido del archivo solicitado
            byte[] buffer = new byte[1024];
            int bytesRead;
            randomFile.seek(start); // Mover a la posición inicial en el archivo

            try (var outputStream = response.getOutputStream()) {
                while ((bytesRead = randomFile.read(buffer)) != -1) {
                    if (start + bytesRead - 1 > end) {
                        outputStream.write(buffer, 0, (int) (end - start + 1));
                        break;
                    }
                    outputStream.write(buffer, 0, bytesRead);
                    start += bytesRead;
                }
                outputStream.flush();
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el video: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/get-videos-by-course/{cursoId}")
    public ResponseEntity<List<Video>> getVideosByCourseId(@PathVariable UUID cursoId) {
        try {
            // Obtener todos los videos asociados al curso usando el cursoId
            List<Video> videos = (videoServiceImpl.findByCursoId(cursoId))
                    .stream()
                    .filter(video -> cursoId.equals(video.getCursoId()))
                    .collect(Collectors.toList());

            if (videos.isEmpty()) {
                // Retornar 404 si no se encuentran videos
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Retornar los videos encontrados
            return new ResponseEntity<>(videos, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
