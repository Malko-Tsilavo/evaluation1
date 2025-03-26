package site.easy.to.build.crm.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.DepenseDTO;
import site.easy.to.build.crm.entity.TauxAlerte;
import site.easy.to.build.crm.repository.TauxAlerteRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depense.DepenseService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/depenses")
public class ApiDepenseController {

    @Autowired
    private DepenseService depenseService;
    @Autowired
    private TauxAlerteRepository tauxAlerteRepository;
    @Autowired
    private BudgetService budgetService;

    // Endpoint pour récupérer les dépenses des tickets par customer
    @GetMapping("/all")
    public List<DepenseDTO> getDepensesAvecTicket() {
        List<Depense> depenses = depenseService.findAll();
        return depenses.stream().map(depense -> {
            DepenseDTO dto = new DepenseDTO();
            dto.setIdDepense(depense.getIdDepense());
            dto.setMontant(depense.getMontant());
            dto.setDateDepense(depense.getDateDepense());
            dto.setTicketId(depense.getTicket() != null ? depense.getTicket().getTicketId() : 0);
            dto.setBudgetId(depense.getBudget().getId());
            dto.setLeadId(depense.getLead() != null ? depense.getLead().getLeadId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping("/modifier-montant")
    public void modifMontantDepense(
            @RequestParam int id,
            @RequestParam Double nouveauMontant) {
        Depense depense = depenseService.findByDepenseId(id);
        if (depense.getMontant() > nouveauMontant) {
            Double diff=depense.getMontant()-nouveauMontant;
            Budget budget = depense.getBudget();
            budget.setMontantRestant(budget.getMontantRestant()+diff);
            budgetService.save(budget);
        } else {
            Double diff=nouveauMontant-depense.getMontant();
            Budget budget = depense.getBudget();
            budget.setMontantRestant(budget.getMontantRestant()-diff);
            budgetService.save(budget);
        }
        depense.setMontant(nouveauMontant);
        depenseService.save(depense);
    }

    @PostMapping("/modifier-taux")
    public void modifTaux(
            @RequestParam Double taux) {
        TauxAlerte tauxAlerte = tauxAlerteRepository.findById(1).orElseThrow(null);
        if (tauxAlerte != null && taux != null) {
            tauxAlerte.setTaux(taux);
            tauxAlerteRepository.save(tauxAlerte);
        }
        
    }

    @GetMapping("/get-taux")
    public ResponseEntity<Double> getTaux() {
        Double total = tauxAlerteRepository.findAll().get(0).getTaux();
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    @GetMapping("/total-par-ticket")
    public ResponseEntity<Double> getTotalDepenseTicket() {
        Double total = depenseService.getTotalDepenseTicket();
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    @GetMapping("/total-par-lead")
    public ResponseEntity<Double> getTotalDepenseLead() {
        Double total = depenseService.getTotalDepenseLead();
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    @GetMapping("/par-ticket")
    public List<DepenseDTO> getAllDepenseTicket() {
        return depenseService.getAllDepenseTicket().stream().map(depense -> {
            DepenseDTO dto = new DepenseDTO();
            
            dto.setIdDepense(depense.getIdDepense());
            dto.setMontant(depense.getMontant());
            dto.setDateDepense(depense.getDateDepense());

            if (depense.getTicket() != null) {
                dto.setTicketId(depense.getTicket().getTicketId());
                dto.setTicketSubject(depense.getTicket().getSubject());
            }

            if (depense.getBudget() != null) {
                dto.setBudgetId(depense.getBudget().getId());
                String nom="budget num "+depense.getBudget().getId()+" : "+depense.getBudget().getMontant();
                dto.setBudgetNom(nom);
            }

            if (depense.getLead() != null) {
                dto.setLeadId(depense.getLead().getLeadId());
                dto.setLeadNom(depense.getLead().getName());
            }

            if (depense.getBudget().getCustomer() != null) {
                dto.setClientId(depense.getBudget().getCustomer().getCustomerId());
                dto.setClientNom(depense.getBudget().getCustomer().getName());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    // Récupérer toutes les dépenses avec un lead
    @GetMapping("/par-lead")
    public List<DepenseDTO> getAllDepenseLead() {
        return depenseService.getAllDepenseLead().stream().map(depense -> {
            DepenseDTO dto = new DepenseDTO();
            dto.setIdDepense(depense.getIdDepense());
            dto.setMontant(depense.getMontant());
            dto.setDateDepense(depense.getDateDepense());

            if (depense.getTicket() != null) {
                dto.setTicketId(depense.getTicket().getTicketId());
                dto.setTicketSubject(depense.getTicket().getSubject());
            }

            if (depense.getBudget() != null) {
                dto.setBudgetId(depense.getBudget().getId());
                String nom="budget num "+depense.getBudget().getId()+" : "+depense.getBudget().getMontant();
                dto.setBudgetNom(nom);
            }

            if (depense.getLead() != null) {
                dto.setLeadId(depense.getLead().getLeadId());
                dto.setLeadNom(depense.getLead().getName());
            }

            if (depense.getBudget().getCustomer() != null) {
                dto.setClientId(depense.getBudget().getCustomer().getCustomerId());
                dto.setClientNom(depense.getBudget().getCustomer().getName());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/budgets-par-customer")
    public ResponseEntity<List<Map<String, Object>>> getBudgetMontantsParCustomer() {
        return ResponseEntity.ok(depenseService.getBudgetMontantsParCustomer());
    }

    // Endpoint pour les montants des dépenses par customer
    @GetMapping("/depenses-par-customer")
    public ResponseEntity<List<Map<String, Object>>> getDepenseMontantsParCustomer() {
        return ResponseEntity.ok(depenseService.getDepenseMontantsParCustomer());
    }

    // Endpoint pour les totaux des montants tickets et leads
    @GetMapping("/total-tickets-leads")
    public ResponseEntity<Map<String, Double>> getTotalMontantsTicketsEtLeads() {
        return ResponseEntity.ok(depenseService.getTotalMontantsTicketsEtLeads());
    }

    @GetMapping("/budget-par-lead")
    public ResponseEntity<List<Map<String, Object>>> getBudgetMontantsParLead() {
        List<Map<String, Object>> result = depenseService.getBudgetMontantsParLead();
        return ResponseEntity.ok(result);
    }

}
