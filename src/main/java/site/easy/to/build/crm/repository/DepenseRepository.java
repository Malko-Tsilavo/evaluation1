package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.Depense;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    // Recherche d'une dépense par montant
    Optional<Depense> findByMontant(double montant);

    // Recherche d'une dépense par id
    Optional<Depense> findById(int id);

    // Recherche des dépenses par date
    List<Depense> findByDateDepense(LocalDateTime dateDepense);

    List<Depense> findByBudgetId(int budgetId);

}
