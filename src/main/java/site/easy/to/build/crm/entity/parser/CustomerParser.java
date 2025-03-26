package site.easy.to.build.crm.entity.parser;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.exception.MultipleException;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.EmailTokenUtils;

public class CustomerParser {
    // final static String DEFAULT_PASSWORD = "1234";
    final static String DEFAULT_COUNTRY = "Madagascar";
    static final String DEFAULT_MANAGER = "ramilisonmalko@gmail.com";


    CustomerLoginInfo customerLoginInfo;
    Customer customer;

    public CustomerParser (UserService userService, String customerEmail, String customerName) throws MultipleException, Exception {
        customer = new Customer();
        customer.setName(customerName);
        customer.setCountry(DEFAULT_COUNTRY);
        customer.setEmail(customerEmail);
        if (userService.findByEmail(DEFAULT_MANAGER) == null) {
            MultipleException exception = new MultipleException();
            exception.getErrors().add("Can't find default user");

            throw exception;
        }
        customer.setUser(userService.findByEmail(DEFAULT_MANAGER));

        customerLoginInfo = new CustomerLoginInfo();
        customerLoginInfo.setCustomer(customer);
        customerLoginInfo.setToken(EmailTokenUtils.generateToken());
        customerLoginInfo.setPasswordSet(true);
        customerLoginInfo.setPassword(new BCryptPasswordEncoder().encode("1234"));
    }

    public CustomerLoginInfo getCustomerLoginInfo() {
        return customerLoginInfo;
    }

    public void setCustomerLoginInfo(CustomerLoginInfo customerLoginInfo) {
        this.customerLoginInfo = customerLoginInfo;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
