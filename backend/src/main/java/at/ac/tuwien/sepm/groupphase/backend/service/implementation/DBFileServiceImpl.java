package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.entity.DBFile;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.DBFileRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.DBFileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DBFileServiceImpl implements DBFileService {

    private final DBFileRepository dbFileRepository;

    public DBFileServiceImpl(DBFileRepository dbFileRepository) {
        this.dbFileRepository = dbFileRepository;
    }

    @Override
    public DBFile getFile(Long id) {
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

            DBFile dbFile = DBFile.builder().fileName(fileName).fileType(file.getContentType()).data(file.getBytes()).build();

            Long id = dbFileRepository.save(dbFile).getId();
            return id;
        } catch (IOException e) {
            throw new ServiceException("File '" + fileName + "' could not be stored: Please try again!", e);
        }
    }
}
