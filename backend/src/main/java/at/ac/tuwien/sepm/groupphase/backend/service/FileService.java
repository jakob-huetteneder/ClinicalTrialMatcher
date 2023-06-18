package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service for managing files.
 */
public interface FileService {

    /**
     * Upload a file to filesystem.
     *
     * @param uploadDir     directory to upload file
     * @param fileName      name of file
     * @param multipartFile filestream of multipart request
     * @param id            of examination
     */
    void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, Long id) throws IOException;

    /**
     * Get file from filesystem.
     *
     * @param id of file
     * @return file as byte array
     */
    byte[] getFile(Long id) throws IOException;

    /**
     * Delete file from filesystem.
     *
     * @param id of file
     */
    void delete(Long id) throws IOException;
}
