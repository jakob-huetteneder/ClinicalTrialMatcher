package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * Upload a file to filesystem.
     *
     * @param uploadDir directory to upload file
     * @param fileName name of file
     * @param multipartFile filestream of multipart request
     * @param id of examination
     */
    void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, Long id) throws IOException;

    byte[] getFile(Long id) throws IOException;

    void delete(Long id) throws IOException;
}
