package site.easy.to.build.crm.entity.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.exception.MultipleException;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;

public class DepenseParser {
    static final String TYPE_LEAD = "lead";
    static final String TYPE_TICKET = "ticket";
    static final String DEFAULT_PRIORITY = "medium";
    // static final String DEFAULT_MANAGER = "nyavorakotonirina5@gmail.com";
    // static final String DEFAULT_EMPLPOYEE = "nyavorakotonirina5@gmail.com";

    Depense depense;

    public Depense getDepense() {
        return depense;
    }

    public void setDepense(Depense depense) {
        this.depense = depense;
    }

    public DepenseParser (CustomerService customerService, UserService userService, String customerEmail, String employeeEmail, String managerEmail, String expense, String subjectOrName, String type, String status) throws MultipleException, Exception {
        double expenseValue = Double.parseDouble(expense.replace(',', '.'));
        Customer customer = customerService.findByEmail(customerEmail);
        LocalDateTime createdAt = LocalDateTime.now();

        MultipleException exception = new MultipleException();

        if (customer == null) {
            exception.getErrors().add("Customer not found in table");
        }

        User manager = userService.findByEmail(managerEmail);
        User employee = userService.findByEmail(employeeEmail);
        depense = new Depense();
        depense.setMontant(expenseValue);
        depense.setDateDepense(LocalDateTime.now());
        
        if (type.equals(TYPE_LEAD)) {
            Lead lead = new Lead();
            lead.setName(subjectOrName);
            lead.setStatus(status);
            lead.setCustomer(customer);
            lead.setManager(manager);
            lead.setEmployee(employee);
            lead.setCreatedAt(createdAt);

            depense.setLead(lead);

        } else if (type.equals(TYPE_TICKET)) {
            Ticket ticket = new Ticket();
            ticket.setSubject(subjectOrName);
            ticket.setStatus(status);
            ticket.setCustomer(customer);
            ticket.setEmployee(employee);
            ticket.setManager(manager);
            ticket.setCreatedAt(createdAt);
            ticket.setPriority(DEFAULT_PRIORITY);

            depense.setTicket(ticket);

        } else {
            exception.getErrors().add("Can't find type '" + type + "' present in CSV file");
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }
    }
}
