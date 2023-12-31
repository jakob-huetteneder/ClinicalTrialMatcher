package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Examination;
import at.ac.tuwien.sepm.groupphase.backend.entity.MedicalImage;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ExaminationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MedicalImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MedicalImageRepository medicalImageRepository;
    private final ExaminationRepository examinationRepository;

    @Autowired
    public FileServiceImpl(MedicalImageRepository medicalImageRepository, ExaminationRepository examinationRepository) {
        this.medicalImageRepository = medicalImageRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile, Long id) throws IOException {
        LOG.trace("saveFile({}, {}, {}, {})", uploadDir, fileName, multipartFile, id);
        Optional<Examination> examination = examinationRepository.findById(id);
        if (examination.isEmpty()) {
            throw new NotFoundException("Examination with id " + id + "does not exist.");
        }

        Optional<MedicalImage> existingImage = medicalImageRepository.findMedicalImageByExamination_Id(id);
        if (existingImage.isPresent()) {
            Files.delete(Paths.get("./src/main/resources/files/" + existingImage.get().getImage()));
            medicalImageRepository.delete(existingImage.get());
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
        LOG.trace("getFile({})", id);
        String path = resolvePath(id);

        return Files.readAllBytes(Paths.get(path));
    }

    @Override
    public void delete(Long id) throws IOException {
        LOG.trace("delete({})", id);
        String path = resolvePath(id);

        Files.delete(Paths.get(path));

        Optional<MedicalImage> image = medicalImageRepository.findMedicalImageByExamination_Id(id);
        if (image.isEmpty()) {
            throw new NotFoundException("No medical image found for examination with id " + id + ".");
        }

        medicalImageRepository.delete(image.get());
    }

    private String resolvePath(Long id) {
        LOG.trace("resolvePath({})", id);
        Optional<Examination> examination = examinationRepository.findById(id);
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
