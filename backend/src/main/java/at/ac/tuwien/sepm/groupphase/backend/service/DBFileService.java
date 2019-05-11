package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.DBFile;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface DBFileService {

    /**
     * Find a single news entry by id.
     *
     * @param id the id of the news entry
     * @return the news entry
     */
    DBFile getFile(Long id);


    /**
     * Store a single file
     *
     * @param file to be stored
     * @return id of stored file
     */
    Long storeFile(MultipartFile file) throws ServiceException;

}
