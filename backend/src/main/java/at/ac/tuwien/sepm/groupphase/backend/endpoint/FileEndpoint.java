package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.entity.DBFile;
import at.ac.tuwien.sepm.groupphase.backend.service.DBFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/files")
@Api(value = "files")
public class FileEndpoint {

    private final DBFileService dbFileService;

    public FileEndpoint(DBFileService dbFileService) {
        this.dbFileService = dbFileService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get file by id", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<Resource> findById(@PathVariable Long id) {
        DBFile dbfile = dbFileService.getFile(id);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(dbfile.getFileType()))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbfile.getFileName() + "\"")
            .body(new ByteArrayResource((dbfile.getData())));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Store file", authorizations = {@Authorization(value = "apiKey")})
    public Long storeFile(@RequestParam("file") MultipartFile file) {
        try {
            return dbFileService.storeFile(file);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
