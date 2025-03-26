package site.easy.to.build.crm.service.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.parser.BudgetParser;
import site.easy.to.build.crm.entity.parser.CustomerParser;
import site.easy.to.build.crm.entity.parser.DepenseParser;
import site.easy.to.build.crm.exception.MultipleException;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

@Service
public class CsvImportService {

    private static final Logger logger = LoggerFactory.getLogger(CsvImportService.class);

    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final UserService userService;
    private final TicketService ticketService;
    private final BudgetService budgetService;
    private final DepenseService depenseService;
    private final LeadService leadService;
    private final Validator validator;
    private final char delimitor = ';';

    public CsvImportService(CustomerService customerService, UserService userService, TicketService ticketService, 
        Validator validator, CustomerLoginInfoService customerLoginInfoService, BudgetService budgetService,DepenseService depenseService,
        LeadService leadService) {
        this.customerService = customerService;
        this.userService = userService;
        this.ticketService = ticketService;
        this.validator = validator;
        this.customerLoginInfoService = customerLoginInfoService;
        this.budgetService = budgetService;
        this.depenseService = depenseService;
        this.leadService = leadService;
    }

    @Transactional
    public void importCsv(MultipartFile file) {
        int numLine = 1;
        List<String> errors = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
    
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                try {
                    tickets.add(processLine(record, numLine, errors));
                } catch (Exception e) {
                    // Logger l'erreur
                    // logger.error("Erreur lors de la lecture du fichier CSV : {}", e.getMessage());

                    e.printStackTrace();
                }
                numLine++;
            }
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier CSV : {}", e.getMessage());
        }

        if (errors.isEmpty()) {
            for (Ticket ticket : tickets) {
                ticketService.save(ticket);
            }
        }
    }

    private Ticket processLine(CSVRecord record, int numLine, List<String> errors) throws Exception {
        String subject = record.get("subject");
        String description = record.get("description");
        String status = record.get("status");
        String priority = record.get("priority");

        User manager = userService.findById((Integer.parseInt(record.get("manager_id"))));
        User employee = userService.findById((Integer.parseInt(record.get("employee_id"))));
        Customer customer = customerService.findByCustomerId((Integer.parseInt(record.get("customer_id"))));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(record.get("created_at"), formatter);

        Ticket ticket = new Ticket(subject, description, status, priority, manager, employee, customer, dateTime);

        Set<ConstraintViolation<Ticket>> violations = validator.validate(ticket);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Ticket> violation : violations) {
                logger.error("Erreur a la ligne {} : {}", numLine, violation.getMessage());
                errors.add(violation.getMessage());
            }
        }

        return ticket;
    }

    @Transactional
    public List<String> importCompleteData (MultipartFile customerFile, MultipartFile budgetFile, MultipartFile depenseFile) {
        int numLine = 1;
        List<String> errors = new ArrayList<>();
        List<CustomerParser> customerParsers = new ArrayList<>();
        List<BudgetParser> budgetParsers = new ArrayList<>();
        List<DepenseParser> depenseParsers = new ArrayList<>();
        
        try (Reader reader = new BufferedReader(new InputStreamReader(customerFile.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimitor).withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                try {
                    customerParsers.add(processCustomerLine(record, numLine, errors));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                numLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errors.isEmpty()) {
            for (CustomerParser customerParser : customerParsers) {
                CustomerLoginInfo costumerLoginInfo = customerLoginInfoService.save(customerParser.getCustomerLoginInfo());
                customerParser.getCustomer().setCustomerLoginInfo(costumerLoginInfo);
                customerService.save(customerParser.getCustomer());
            }
        }

        numLine = 1;
        try (Reader reader = new BufferedReader(new InputStreamReader(budgetFile.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimitor).withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                try {
                    budgetParsers.add(processBudgetLine(record, numLine, errors));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                numLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errors.isEmpty()) {
            for (BudgetParser budgetParser : budgetParsers) {
                budgetService.save(budgetParser.getBudget());
            }
        }

        numLine = 1;
        try (Reader reader = new BufferedReader(new InputStreamReader(depenseFile.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(delimitor).withFirstRecordAsHeader())) {
            for (CSVRecord record : csvParser) {
                try {
                    depenseParsers.add(processDepenseLine(record, numLine, errors));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                numLine++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (errors.isEmpty()) {
            for (DepenseParser depenseParser : depenseParsers) {
                if (depenseParser.getDepense().getLead() != null) {
                    Lead lead = leadService.save(depenseParser.getDepense().getLead());
                    depenseParser.getDepense().setLead(lead);
                    depenseService.save(depenseParser.getDepense());
                } else {
                    Ticket ticket = ticketService.save(depenseParser.getDepense().getTicket());
                    depenseParser.getDepense().setTicket(ticket);
                    depenseService.save(depenseParser.getDepense());
                }
            }
        }

        if (!errors.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return errors;
    }

    public CustomerParser processCustomerLine (CSVRecord record, int numLine, List<String> errors) throws Exception {
        CustomerParser customerParser = null;
        
        try {
            customerParser = new CustomerParser(userService, record.get("customer_email"), record.get("customer_name"));

            Set<ConstraintViolation<Customer>> violations = validator.validate(customerParser.getCustomer());
            if (!violations.isEmpty()) {
                for (ConstraintViolation<Customer> violation : violations) {
                    logger.error("Erreur a la ligne {} pour Customer : {}", numLine, violation.getMessage());
                    errors.add(violation.getMessage());
                }
            }

        } catch (MultipleException e) {
            for (String error : e.getErrors()) {
                errors.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return customerParser;
    }

    public BudgetParser processBudgetLine (CSVRecord record, int numLine, List<String> errors) throws Exception {
        BudgetParser budgetParser = null;
        
        try {
            budgetParser = new BudgetParser(customerService, record.get("customer_email"), record.get("Budget"));

            Set<ConstraintViolation<Budget>> violations = validator.validate(budgetParser.getBudget());

            if (!violations.isEmpty()) {
                for (ConstraintViolation<Budget> violation : violations) {
                    logger.error("Erreur a la ligne {} pour Budget : {}", numLine, violation.getMessage());
                    errors.add(violation.getMessage());
                }
            }

        } catch (MultipleException e) {
            for (String error : e.getErrors()) {
                errors.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return budgetParser;
    }

    public DepenseParser processDepenseLine (CSVRecord record, int numLine, List<String> errors) throws Exception {
        DepenseParser depenseParser = null;
        
        try {
            depenseParser = new DepenseParser(customerService, userService, record.get("customer_email"), null, null, record.get("expense"), record.get("subject_or_name"), record.get("type"), record.get("status"));

            if (depenseParser.getDepense().getTicket() != null) {
                Set<ConstraintViolation<Ticket>> violations = validator.validate(depenseParser.getDepense().getTicket());

                for (ConstraintViolation<Ticket> violation : violations) {
                    logger.error("Erreur a la ligne {} pour Ticket : {}", numLine, violation.getMessage());
                    errors.add(violation.getMessage());
                }
            } else {
                Set<ConstraintViolation<Lead>> violations = validator.validate(depenseParser.getDepense().getLead());

                for (ConstraintViolation<Lead> violation : violations) {
                    logger.error("Erreur a la ligne {} pour Lead : {}", numLine, violation.getMessage());
                    errors.add(violation.getMessage());
                }
            }

            Set<ConstraintViolation<Depense>> violations = validator.validate(depenseParser.getDepense());

            if (!violations.isEmpty()) {
                for (ConstraintViolation<Depense> violation : violations) {
                    logger.error("Erreur a la ligne {} pour Depense : {}", numLine, violation.getMessage());
                    errors.add(violation.getMessage());
                }
            }

        } catch (MultipleException e) {
            for (String error : e.getErrors()) {
                errors.add(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 

        return depenseParser;
    }
}