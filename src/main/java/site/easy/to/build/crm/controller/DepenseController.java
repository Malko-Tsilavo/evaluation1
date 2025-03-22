package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.TauxAlerteRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

@Controller

public class DepenseController {

    @Autowired
    private DepenseService depenseService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private LeadService leadService;
    @Autowired
    private TauxAlerteRepository tauxAlerteRepository;
    @Autowired
    private BudgetService budgetService;
    
    @GetMapping("/depenses/all")
    public String getDepensesAvecTicket(Model model) {
        model.addAttribute("depenses", depenseService.findAll());
        return "depense/list-depense";  // Page de liste des dépenses
    }

    @GetMapping("/depenses-insert/{id}")
    public String showForm(@PathVariable("id") int id,Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        model.addAttribute("leads", leadService.findAll());
        model.addAttribute("budgets", budgetService.finBudgetsByCustomerId(id));
        model.addAttribute("taux", tauxAlerteRepository.findAll().get(0));
        return "depense/insert";
    }

    @PostMapping("/depenses/save")
    public String insert(@RequestParam("budgetId") int budgetId,@RequestParam("ticketId") String ticketIdT,
    @RequestParam("leadId") String leadIdT,
    @ModelAttribute Depense depense){
        Budget budget=budgetService.findByBudgetId(budgetId);
        if (!ticketIdT.equalsIgnoreCase("")) {
            int ticketId=Integer.parseInt(ticketIdT);
            Ticket ticket=ticketService.findByTicketId(ticketId);
            if (ticket != null) {
                depense.setTicket(ticket);
            }
        }
        if (!leadIdT.equalsIgnoreCase("")) {
            int leadId=Integer.parseInt(leadIdT);
            Lead lead=leadService.findByLeadId(leadId);
        
            if (lead != null) {
                depense.setLead(lead);
            }
        }
        
        depense.setBudget(budget);
        budget.setMontantRestant(budget.getMontantRestant()-depense.getMontant());
        budgetService.save(budget);
        depenseService.save(depense);
        return "redirect:/depenses/all";
    }
}
