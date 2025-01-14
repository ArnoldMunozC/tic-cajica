package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.services.VideoService;
import com.amunoz.springboot.webflux.ticcajica.services.impl.VideoServiceImpl;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
@CrossOrigin(origins = "http://localhost:4200")
public class VideoController {

    private final VideoService videoService;
    private final VideoServiceImpl videoServiceImpl;

    public VideoController(VideoService videoService, VideoServiceImpl videoServiceImpl) {
        this.videoService = videoService;
        this.videoServiceImpl = videoServiceImpl;
    }

    private final String videoDirectory = "./Users/arnmunoz/aprendizaje/videos";

    @PostMapping(value = "/create-video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createCourse(
            @RequestPart("video") List<Video> videos,
            @RequestParam("file") List<MultipartFile> files,
            @RequestParam("cursoId") String cursoId) {

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
                videoService.save(currentCourse);
            }

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Obtiene todos los cursos
     * @return Lista de cursos
     */

    @GetMapping("/get-video/{id}")
    public Video getCourse(@PathVariable Long id) {
        Video video = videoService.findById(id);
        return video;
    }


    @DeleteMapping("/delete-video/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        // Buscar el curso por su ID
        Video video = videoService.findById(id);

        if (video!= null) {
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
                videoService.delete(id);

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
            videoService.update(currentCourse, currentCourse.getId());

            return new ResponseEntity<>(HttpStatus.OK); // Retornar 200 si la actualización fue exitosa

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/videos/{filename}")
    public ResponseEntity<FileSystemResource> getVideo(@PathVariable String filename) {
        File videoFile = new File(videoDirectory, filename);
        if (!videoFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(videoFile));
    }


}
