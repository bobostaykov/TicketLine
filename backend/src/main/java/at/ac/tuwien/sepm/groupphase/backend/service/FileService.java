package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * Find a single image associated to a news entry by id.
     *
     * @param id the id of the image
     * @return the image
     */
    File getFile(Long id);

    /**
     * Store a single image associated to a news entry
     *
     * @param file file to be stored
     * @return id of stored file
     */
    Long storeFile(MultipartFile file) throws ServiceException;

}
