package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/files")
public class FilesEndpoint {

    private final FileService fileService;

    public FilesEndpoint(FileService fileService) {
        this.fileService = fileService;
    }

    @PermitAll
    @PostMapping("{id}")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws IOException, HttpMediaTypeNotSupportedException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (!(extension.equals(".png") || extension.equals(".jpg"))) {
            throw new HttpMediaTypeNotSupportedException("Not supported:" + extension);
        }
        fileService.saveFile("./src/main/resources/files/", fileName, multipartFile, id);
    }

    @PermitAll
    @GetMapping("{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("multipart/form-data"));
        headers.setContentDispositionFormData("attachment", null);
        headers.setCacheControl("no-cache");

        byte[] img = fileService.getFile(id);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @PermitAll
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException {
        fileService.delete(id);
    }

}
