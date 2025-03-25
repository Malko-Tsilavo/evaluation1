
package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.BudgetDetails;
import site.easy.to.build.crm.service.budget.BudgetDetailsService;
import site.easy.to.build.crm.service.budget.BudgetService;

@Controller
@RequestMapping("/budget-details")
public class BudgetDetailsController {

    private final BudgetDetailsService budgetDetailsService;
    private final BudgetService budgetService;

    @Autowired
    public BudgetDetailsController(BudgetDetailsService budgetDetailsService, BudgetService budgetService) {
        this.budgetDetailsService = budgetDetailsService;
        this.budgetService = budgetService;
    }

    @GetMapping("/list/{id}")
    public String showBudgetDetailsList(@PathVariable("id") int budgetId, Model model) {
        model.addAttribute("budgetDetails", budgetDetailsService.findByBudgetId(budgetId));
        model.addAttribute("budgetId", budgetId);
        return "budget-details/list";
    }

    @GetMapping("/create/{id}")
    public String showCreateForm(@PathVariable("id") int budgetId, Model model) {
        model.addAttribute("budgetDetails", new BudgetDetails());
        model.addAttribute("budgetId", budgetId);
        return "budget-details/create";
    }

    @PostMapping("/create/{id}")
    @Transactional
    public String saveBudgetDetails(@PathVariable("id") int budgetId, @ModelAttribute BudgetDetails budgetDetails) {
        Budget budget=budgetDetails.getBudget();
        budget.setMontant(budget.getMontant()+budgetDetails.getMontant());
        budgetService.save(budget);
        budgetDetailsService.save(budgetDetails);
        return "redirect:/budget-details/list/" + budgetId;
    }

    @GetMapping("/delete/{id}/{budgetId}")
    public String deleteBudgetDetails(@PathVariable("id") int id, @PathVariable("budgetId") int budgetId) {
        BudgetDetails budgetDetails = budgetDetailsService.findById(id);
        Budget budget=budgetDetails.getBudget();
        budget.setMontant(budget.getMontant()-budgetDetails.getMontant());
        budgetService.save(budget);
        budgetDetailsService.delete(budgetDetails);
        return "redirect:/budget-details/list/" + budgetId;
    }
}
