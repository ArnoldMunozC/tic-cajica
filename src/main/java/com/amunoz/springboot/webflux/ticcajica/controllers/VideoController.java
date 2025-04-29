package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.services.impl.CursoServiceImpl;
import com.amunoz.springboot.webflux.ticcajica.services.impl.VideoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/api/v1/video")
public class VideoController {

    private final VideoServiceImpl videoServiceImpl;
    private final CursoServiceImpl cursoServiceImpl;

    public VideoController(VideoServiceImpl videoServiceImpl, CursoServiceImpl cursoServiceImpl) {
        this.videoServiceImpl = videoServiceImpl;
        this.cursoServiceImpl = cursoServiceImpl;
    }


    // Agregar video al curso
    @PostMapping(value = "/create-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSingleVideo(
            @RequestParam("title") String title,      // El título del video
            @RequestPart("file") MultipartFile file, // El archivo de video
            @RequestParam("cursoId") UUID cursoId,    // ID del curso
            Model model) {
        try {
            // Validar que se haya proporcionado un archivo válido
            if (file.isEmpty()) {
                model.addAttribute("error", "El archivo está vacío. Por favor, selecciona uno válido.");
                return "error"; // Retornar a una plantilla de error
            }

            // Nombre del archivo
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                model.addAttribute("error", "El nombre del archivo es inválido.");
                return "error"; // Retornar a una plantilla de error
            }

            // Guardar el archivo en almacenamiento local
            Path filePath = Paths.get("C:/Users/Arnold Mauricio/Documents/videos", originalFilename);
            Files.write(filePath, file.getBytes());

            // Crear el objeto Video y guardar en base de datos
            Video video = new Video();
            video.setTitle(title);
            video.setVideoPath(filePath.toString());
            video.setCursoId(cursoId);
            videoServiceImpl.save(video);

            return "redirect:/api/v1/curso/byId/" + cursoId;

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrió un error al guardar el archivo. Inténtalo de nuevo.");
            return "error"; // Retornar a una plantilla de error
        }
    }


    // formulario para agregar video
    @GetMapping("/form-add-videos/{id}")
    public String showFormCreateVideo(@PathVariable UUID id, Model model) {
        model.addAttribute("cursoId", id);
        return "form-add-videos";
    }

    // formulario para actualizar video
    @GetMapping("/form-update-video/{idVideo}/cursoId/{idCurso}")
    public String showFormUpdateVideo(@PathVariable Long idVideo, @PathVariable UUID idCurso, Model model) {
        model.addAttribute("idVideo", idVideo);
        model.addAttribute("idCurso", idCurso);
        model.addAttribute("texto", "update Video");
        return "form-update-video";
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


    @PostMapping(value = "/update-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateCourse(
            @RequestPart("title") String title,
            @RequestParam("file") MultipartFile file,
            @RequestParam("videoId") Long videoId,
            @RequestParam("idCurso") UUID idCurso,
            Model model) {

        try {
            // Obtener el ID del curso desde el cuerpo (objeto course)
            UUID cursoId = idCurso;

            // Buscar el curso actual por su ID
            Video currentCourse = videoServiceImpl.findById(videoId);

            // Si el curso no existe, retornar 404
            if (currentCourse == null) {
                model.addAttribute("error", "No existe el curso!");
                return "error";
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
            currentCourse.setTitle(title);

            // Guardar la actualización del curso en la base de datos
            videoServiceImpl.update(currentCourse, currentCourse.getId());

            return "redirect:/api/v1/curso/byId/" + cursoId;
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrio un error :" + e.getMessage());
            return "error";
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

    // devuelve los videos del curso
    @GetMapping("/get-video/{id}/cursoId/{cursoId}")
    public String getCourse2(@PathVariable Long id, @PathVariable UUID cursoId, Model model) {
        // Obtenemos el objeto Video desde el servicio
        try {
            Video video = videoServiceImpl.findById(id);
            if (video == null) {
                throw new RuntimeException("El video con id " + id + " no fue encontrado.");
            }

            Curso curso = cursoServiceImpl.findById(cursoId);

            // Verificar si cursoId ya es un UUID válido
            UUID uuid = UUID.fromString(cursoId.toString());
            List<Video> videos = videoServiceImpl.findByCursoId(uuid);
            curso.setVideos(videos);
            model.addAttribute("curso", curso);
            if (videos == null || videos.isEmpty()) {
                throw new RuntimeException("No hay videos asociados al curso con ID " + cursoId);
            }
            // Extraer el nombre del archivo del video
            String videoFileName = new File(video.getVideoPath()).getName(); // ejemplo: "video1.mp4"
            model.addAttribute("videoPath", "/videos/" + videoFileName); // Ruta accesible desde el navegador
            model.addAttribute("videos", videos);
            model.addAttribute("curso", curso);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El cursoId proporcionado no es un UUID válido: " + cursoId);
        }
        return "detalleCurso";
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
