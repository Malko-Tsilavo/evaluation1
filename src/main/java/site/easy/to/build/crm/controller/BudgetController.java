package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.budget.BudgetService;

@Controller
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/list")
    public String showBudgetList(Model model) {
        model.addAttribute("budgets", budgetService.findAll());
        return "budget/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("budget", new Budget());
        return "budget/create";
    }

    @PostMapping("/create")
    @Transactional
    public String saveBudget(@ModelAttribute Budget budget, Model model) {
        budgetService.save(budget);
        return "redirect:/budget/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Budget budget = budgetService.findByBudgetId(id);
        model.addAttribute("budget", budget);
        return "budget/update";
    }

    @PostMapping("/update")
    @Transactional
    public String updateBudget(@ModelAttribute Budget budget, Model model) {
        budgetService.save(budget);
        return "redirect:/budget/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable("id") int id) {
        Budget budget = budgetService.findByBudgetId(id);
        budgetService.delete(budget);
        return "redirect:/budget/list";
    }

    @GetMapping("/detail/{id}")
    public String showBudgetDetail(@PathVariable("id") int id, Model model) {
        Budget budget = budgetService.findByBudgetId(id);
        model.addAttribute("budget", budget);
        return "budget/detail";
    }
}
