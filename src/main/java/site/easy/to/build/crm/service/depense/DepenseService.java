package site.easy.to.build.crm.service.depense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DepenseService {

    private final DepenseRepository depenseRepository;

    @Autowired
    public DepenseService(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    public List<Depense> findAll() {
        return depenseRepository.findAll();
    }

    public void save(Depense depense) {
        depenseRepository.save(depense);
    }

    public Depense findByDepenseId(int id) {
        Optional<Depense> result = depenseRepository.findById(id);
        return result.orElse(null);  // Retourne null si aucune dépense n'est trouvée
    }

    public void delete(Depense depense) {
        depenseRepository.delete(depense);
    }

    public List<Depense> findByDate(LocalDateTime date) {
        return depenseRepository.findByDateDepense(date);
    }

    public List<Depense> findByBudgetId(int budgetId) {
        return depenseRepository.findByBudgetId(budgetId);
    }

    public List<Map<String, Object>> getTicketDepensesByCustomer() {
        return depenseRepository.getTicketDepensesByCustomer();
    }

    // Récupérer les dépenses des leads par customer
    public List<Map<String, Object>> getLeadDepensesByCustomer() {
        return depenseRepository.getLeadDepensesByCustomer();
    }

    public List<Map<String, Object>> getDepensesByLead() {
        return depenseRepository.getDepensesParLead();
    }

    // Récupérer les dépenses des leads par customer
    public List<Map<String, Object>> getDepensesByTicket() {
        return depenseRepository.getDepensesParTicket();
    }

    public List<Depense> getAllDepenseLead() {
        return depenseRepository.getAllDepenseLead();
    }

    public List<Depense> getAllDepenseTicket() {
        return depenseRepository.getAllDepenseTicket();
    }

    public Double getTotalDepenseTicket() {
        return depenseRepository.getTotalDepenseTicket();
    }

    public Double getTotalDepenseLead() {
        return depenseRepository.getTotalDepenseLead();
    }

    // Récupérer les montants des budgets par customer
    public List<Map<String, Object>> getBudgetMontantsParCustomer() {
        return depenseRepository.getBudgetMontantsParCustomer();
    }

    // Récupérer les montants des dépenses par customer
    public List<Map<String, Object>> getDepenseMontantsParCustomer() {
        return depenseRepository.getDepenseMontantsParCustomer();
    }

    // Récupérer les montants totaux pour les tickets et les leads
    public Map<String, Double> getTotalMontantsTicketsEtLeads() {
        Double totalTickets = depenseRepository.getTotalDepenseTicket();
        Double totalLeads = depenseRepository.getTotalDepenseLead();
        
        return Map.of(
            "totalTickets", totalTickets != null ? totalTickets : 0.0,
            "totalLeads", totalLeads != null ? totalLeads : 0.0
        );
    }

     // Méthode pour récupérer la somme des montants des budgets par lead
     public List<Map<String, Object>> getBudgetMontantsParLead() {
        return depenseRepository.findTotalBudgetByLead();
    }
}
