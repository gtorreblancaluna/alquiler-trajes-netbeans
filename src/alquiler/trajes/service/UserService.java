package alquiler.trajes.service;

import alquiler.trajes.config.Encoder;
import alquiler.trajes.config.EncryptionException;
import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.UserDao;
import alquiler.trajes.entity.User;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.DataOriginException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.exceptions.NoDataFoundException;
import static alquiler.trajes.util.Utility.onlyAdminUserAccess;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import org.apache.log4j.Logger;


public class UserService {
    
    private UserService(){}
    
    private static final Logger log = Logger.getLogger(UserService.class.getName());
                
    private UserDao userDao = UserDao.getInstance();
            
    private static final UserService SINGLE_INSTANCE = null;
        
    public static UserService getInstance(){
        
        if (SINGLE_INSTANCE == null) {
            return new UserService();
        }
        return SINGLE_INSTANCE;
    }
    
    public void saveOrUpdate (User user) throws BusinessException {
        onlyAdminUserAccess();
        if (user == null) {
            throw new InvalidDataException(ApplicationConstants.MESSAGE_NOT_PARAMETER_RECEIVED);
        }
        List<String> errors = new ArrayList<>();
        
        if (user.getName().isEmpty()) {
            errors.add("Nombre es requerido.");
        }
        if (user.getLastName().isBlank()) {
            errors.add("Apellidos es requerido.");
        }
        if (!user.getName().isEmpty() && user.getName().length() > 100) {
            errors.add("Limite de caracteres permitidos [100] excedido para el Nombre.");
        }
        if (!user.getLastName().isEmpty() && user.getLastName().length() > 100) {
            errors.add("Limite de caracteres permitidos [100] excedido para los Apellidos.");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(String.join(",", errors));
        }
        
        try {
            Optional<User> userPasswdFinded = finByPasswd(user.getPassword());
            if (userPasswdFinded.isPresent()) {
                throw new InvalidDataException("La contraseña se encuentra en uso, intenta ingresar otra contraseña diferente.");
            }
        } catch (NoDataFoundException e) {
            // nothing to do.
        }
  
        if (user.getId() != null) {
            User existUser = findById (user.getId());
            User userToSave = User.builder()
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .enabled(Boolean.TRUE)
                    .id(user.getId())
                    .roles(existUser.getRoles())
                    .updatedAt(new Date())
                    .password(existUser.getPassword())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
            userDao.update(userToSave);
        } else {
            try {
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());
                user.setEnabled(Boolean.TRUE);
                user.setPassword(Encoder.encrypt(ApplicationConstants.KEY_USERS, user.getPassword()));
                userDao.save(user);
            } catch (EncryptionException e) {
                throw new DataOriginException(e.getMessage(),e);
            }
        }
    }
    
    public Optional<User> finByPasswd (final String passwd)throws BusinessException {

        try {
            String passwordEncrypted = Encoder.encrypt(ApplicationConstants.KEY_USERS, passwd);
            User user = userDao.findByPasswd(passwordEncrypted);
            if (user == null) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND_LOGIN);
            }
            return Optional.of(user);
        } catch (EncryptionException e) {
            log.error(e);
            throw new DataOriginException(e.getMessage(),e);
        }

    }
    
    public User findById (final Long id)throws BusinessException {

        try {
            
            Optional<User> opUser = userDao.get(id);
            if (!opUser.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
            }
            return opUser.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<User> getAll () throws BusinessException {
        return userDao.getAll();
    }
    
}
