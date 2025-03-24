package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.easy.to.build.crm.entity.Depense;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    // Recherche d'une dépense par montant
    Optional<Depense> findByMontant(double montant);

    // Recherche d'une dépense par id
    Optional<Depense> findById(int id);

    // Recherche des dépenses par date
    List<Depense> findByDateDepense(LocalDateTime dateDepense);

    List<Depense> findByBudgetId(int budgetId);

     @Query("SELECT c.id AS customerId, SUM(d.montant) AS totalDepenses " +
           "FROM Depense d " +
           "JOIN d.ticket t " +
           "JOIN t.customer c " +
           "GROUP BY c.id")
    List<Map<String, Object>> getTicketDepensesByCustomer();

    // Récupérer les dépenses des leads par customer
    @Query("SELECT c.id AS customerId, SUM(d.montant) AS totalDepenses " +
           "FROM Depense d " +
           "JOIN d.lead l " +
           "JOIN l.customer c " +
           "GROUP BY c.id")
    List<Map<String, Object>> getLeadDepensesByCustomer();

    @Query("SELECT d.ticket.id AS ticketId, SUM(d.montant) AS totalDepenses " +
    "FROM Depense d WHERE d.ticket IS NOT NULL " +
    "GROUP BY d.ticket.id")
    List<Map<String, Object>> getDepensesParTicket();
       
       // Récupérer les dépenses par lead
       @Query("SELECT d.lead.id AS leadId, SUM(d.montant) AS totalDepenses " +
       "FROM Depense d WHERE d.lead IS NOT NULL " +
       "GROUP BY d.lead.id")
       List<Map<String, Object>> getDepensesParLead();

}
