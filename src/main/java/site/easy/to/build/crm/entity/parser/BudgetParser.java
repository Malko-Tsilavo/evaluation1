package site.easy.to.build.crm.entity.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.exception.MultipleException;
import site.easy.to.build.crm.service.customer.CustomerService;

public class BudgetParser {
    Budget budget;

    public BudgetParser (CustomerService customerService, String customerEmail, String budgetValue) throws MultipleException, Exception {
        LocalDateTime date = LocalDateTime.now();
        Customer customer = customerService.findByEmail(customerEmail);

        budget = new Budget();
        budget.setCustomer(customer);
        budget.setMontant(Double.valueOf(budgetValue));
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
