package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * This class defines the endpoints for the file resource.
 */
@RestController
@RequestMapping(value = "/api/v1/files")
public class FilesEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static final String BASE_PATH = "/api/v1/files";
    private final FileService fileService;

    public FilesEndpoint(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Uploads a file to the server.
     *
     * @param multipartFile the file to upload
     * @param id            the id of the file
     * @throws IOException                        if an error occurs while saving the file
     * @throws HttpMediaTypeNotSupportedException if the file is not supported
     */
    @PermitAll
    @PostMapping("{id}")
    public void upload(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws IOException, HttpMediaTypeNotSupportedException {
        LOG.trace("upload({}, {})", multipartFile, id);
        LOG.info("POST " + BASE_PATH + "/" + id);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        if (!fileName.contains(".")) {
            throw new HttpMediaTypeNotSupportedException("Not supported!");
        }
        String extension = fileName.substring(fileName.lastIndexOf("."));
        if (!(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".PNG"))) {
            throw new HttpMediaTypeNotSupportedException("Not supported:" + extension);
        }
        fileService.saveFile("./src/main/resources/files/", fileName, multipartFile, id);
    }

    /**
     * Downloads a file from the server.
     *
     * @param id the id of the file
     * @return the file
     * @throws IOException if an error occurs while reading the file
     */
    @PermitAll
    @GetMapping("{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        LOG.trace("download({})", id);
        LOG.info("GET " + BASE_PATH + "/" + id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("multipart/form-data"));
        headers.setContentDispositionFormData("attachment", null);
        headers.setCacheControl("no-cache");

        byte[] img = fileService.getFile(id);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    /**
     * Deletes a file from the server.
     *
     * @param id the id of the file
     * @throws IOException if an error occurs while deleting the file
     */
    @PermitAll
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException {
        LOG.trace("delete({})", id);
        LOG.info("DELETE " + BASE_PATH + "/" + id);
        fileService.delete(id);
    }

}
