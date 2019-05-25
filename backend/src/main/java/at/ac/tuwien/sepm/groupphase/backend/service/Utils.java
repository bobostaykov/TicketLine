package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;

import java.security.NoSuchAlgorithmException;

public interface Utils {

    /**
     * Hash the password field of userDTO using MD5
     * @param userDTO user object whose password to encrypt
     * @return user with encrypted password
     */
    UserDTO encryptPassword(UserDTO userDTO) throws NoSuchAlgorithmException;

    /**
     * Send new password to user if forgotten
     * @param recipientEmail email to which to send the new password
     * @return true if the action was successful or false otherwise
     */
    Boolean sendNewPassword(String recipientEmail);

}
