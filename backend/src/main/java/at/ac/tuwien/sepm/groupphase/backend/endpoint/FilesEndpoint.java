package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping
    public void upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam("examination_id") Long id) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        fileService.saveFile("backend/src/main/resources/files/", fileName, multipartFile, id);
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<byte[]> download(@RequestParam("examination_id") Long id) throws IOException {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("multipart/form-data"));
        headers.setContentDispositionFormData("attachment", null);
        headers.setCacheControl("no-cache");

        byte[] img = fileService.getFile(id);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @PermitAll
    @DeleteMapping
    public void delete(@RequestParam("examination_id") Long id) throws IOException {
        fileService.delete(id);
    }

}
