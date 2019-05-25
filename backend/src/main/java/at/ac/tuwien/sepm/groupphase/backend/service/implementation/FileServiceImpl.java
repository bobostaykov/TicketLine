package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DBFileRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.FileService;
<<<<<<< HEAD:backend/src/main/java/at/ac/tuwien/sepm/groupphase/backend/service/implementation/FileServiceImpl.java
import org.apache.commons.io.FilenameUtils;
=======
>>>>>>> 35f99a3b799b050bea44ee017c3608bb2553412f:backend/src/main/java/at/ac/tuwien/sepm/groupphase/backend/service/implementation/FileServiceImpl.java
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {

    private final DBFileRepository dbFileRepository;

    public FileServiceImpl(DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    @Override
    public File getFile(Long id) {
        return dbFileRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Long storeFile(MultipartFile file) throws ServiceException{
        // Normalize file username
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's username contains invalid characters
            if(fileName.contains("..")) {
                throw new ServiceException("File '" + fileName + "' could not be stored: Filename contains invalid path sequence ");
            }

<<<<<<< HEAD:backend/src/main/java/at/ac/tuwien/sepm/groupphase/backend/service/implementation/FileServiceImpl.java
            File dbFile = File.builder().fileName(fileName).fileType(FilenameUtils.getExtension(file.getOriginalFilename())).data(file.getBytes()).build();
=======
            File dbFile = File.builder().fileName(fileName).fileType(file.getContentType()).data(file.getBytes()).build();
>>>>>>> 35f99a3b799b050bea44ee017c3608bb2553412f:backend/src/main/java/at/ac/tuwien/sepm/groupphase/backend/service/implementation/FileServiceImpl.java

            Long id = dbFileRepository.save(dbFile).getId();
            return id;
        } catch (IOException e) {
            throw new ServiceException("File '" + fileName + "' could not be stored: Please try again!", e);
        }
    }
}
