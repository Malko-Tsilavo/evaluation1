package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.BudgetDetails;

import java.util.List;

@Repository
public interface BudgetDetailsRepository extends JpaRepository<BudgetDetails, Integer> {
    List<BudgetDetails> findByBudgetId(int budgetId);  // Récupérer les détails par budget
}
