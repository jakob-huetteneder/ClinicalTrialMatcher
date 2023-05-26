package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserLoginDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Admin;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.MedicalImage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MedicalImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MedicalImageRepository medicalImageRepository;
    private final ExaminationsRepository examinationsRepository;

    @Autowired
    public FileServiceImpl(MedicalImageRepository medicalImageRepository, ExaminationsRepository examinationsRepository) {
        this.medicalImageRepository = medicalImageRepository;
        this.examinationsRepository = examinationsRepository;
    }

    @Override
    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, Long id) throws IOException {

        Optional<Examination> examination = examinationsRepository.findById(id);
        if (examination.isEmpty()) {
            throw new NotFoundException("Examination with id " + id + "does not exist.");
        }

        Path uploadPath = Paths.get(uploadDir);
        UUID uuid = UUID.randomUUID();

        if (!Files.exists(uploadPath)) { //create files path
            Files.createDirectories(uploadPath);
        }

        String image = uuid + fileName.substring(fileName.lastIndexOf('.'));

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(image);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName + " with uuid " + uuid, ioe);
        }

        medicalImageRepository.save(new MedicalImage().setImage(image).setExamination(examination.get()));
    }

    @Override
    public byte[] getFile(Long id) throws IOException {
        String path = resolvePath(id);

        return Files.readAllBytes(Paths.get(path));
    }

    @Override
    public void delete(Long id) throws IOException {

        String path = resolvePath(id);

        Files.delete(Paths.get(path));

        Optional<MedicalImage> image = medicalImageRepository.findMedicalImageByExamination_Id(id);
        if (image.isEmpty()) {
            throw new NotFoundException("No medical image found for examination with id " + id + ".");
        }

        medicalImageRepository.delete(image.get());
    }

    private String resolvePath(Long id) {
        Optional<Examination> examination = examinationsRepository.findById(id);
        if (examination.isEmpty()) {
            throw new NotFoundException("Examination with id " + id + "does not exist.");
        }

        Optional<MedicalImage> image = medicalImageRepository.findMedicalImageByExamination_Id(id);
        if (image.isEmpty()) {
            throw new NotFoundException("No medical image found for examination with id " + id + ".");
        }

        return "./src/main/resources/files/" + image.get().getImage();
    }

}
