package alquiler.trajes.service;

import alquiler.trajes.constant.ApplicationConstants;
import alquiler.trajes.dao.CustomerDao;
import alquiler.trajes.entity.Customer;
import alquiler.trajes.exceptions.BusinessException;
import alquiler.trajes.exceptions.InvalidDataException;
import alquiler.trajes.exceptions.NoDataFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import org.apache.log4j.Logger;


public final class CustomerService {
    
    private CustomerService(){}
    
    private static final Logger log = Logger.getLogger(CustomerService.class.getName());
                
    private CustomerDao customerDao = CustomerDao.getInstance();
            
    private static CustomerService SINGLE_INSTANCE;
        
   public static synchronized CustomerService getInstance() {
        
        if (SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new CustomerService();
        }
        return SINGLE_INSTANCE;
    }
    
    public void saveOrUpdate (Customer customer) throws BusinessException {
        if (customer == null) {
            throw new InvalidDataException(ApplicationConstants.MESSAGE_NOT_PARAMETER_RECEIVED);
        }
        List<String> errors = new ArrayList<>();
        
        if (customer.getName().isEmpty()) {
            errors.add("Nombre es requerido.");
        }
        if (customer.getLastName().isEmpty()) {
            errors.add("Apellidos es requerido.");
        }
        if (customer.getPhoneNumber1().isEmpty()) {
            errors.add("Telefono es requerido.");
        }
        if (!customer.getName().isEmpty() && customer.getName().length() > 100) {
            errors.add("Limite de caracteres permitidos [100] excedido para el Nombre.");
        }
        if (!customer.getLastName().isEmpty() && customer.getLastName().length() > 100) {
            errors.add("Limite de caracteres permitidos [100] excedido para los Apellidos.");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(String.join(",", errors));
        }

  
        if (customer.getId() != null) {
            Customer existCustomer = findById (customer.getId());
            Customer customerToSave = Customer.builder()
                    .name(customer.getName())
                    .lastName(customer.getLastName())
                    .email(customer.getEmail())
                    .enabled(Boolean.TRUE)
                    .id(customer.getId())
                    .updatedAt(new Date())
                    .phoneNumber1(existCustomer.getPhoneNumber1())
                    .phoneNumber2(existCustomer.getPhoneNumber2())
                    .phoneNumber3(existCustomer.getPhoneNumber3())
                    .build();
            customerDao.update(customerToSave);
        } else {
            customer.setCreatedAt(new Date());
            customer.setUpdatedAt(new Date());
            customer.setEnabled(Boolean.TRUE);
            customerDao.save(customer);
        }
    }
    
    
    public Customer findById (final Long id)throws BusinessException {

        try {
            
            Optional<Customer> opUser = customerDao.get(id);
            if (!opUser.isPresent()) {
                throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
            }
            return opUser.get();
        } catch (NoResultException e) {
            log.error(e);
            throw new NoDataFoundException(ApplicationConstants.NO_USER_FOUND);
        }

    }
    
    public List<Customer> getAll () throws BusinessException {
        return customerDao.getAll();
    }
    
}
