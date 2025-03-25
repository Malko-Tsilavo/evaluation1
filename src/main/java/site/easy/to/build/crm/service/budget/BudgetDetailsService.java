package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.BudgetDetails;
import site.easy.to.build.crm.repository.BudgetDetailsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetDetailsService {

    private final BudgetDetailsRepository budgetDetailsRepository;

    @Autowired
    public BudgetDetailsService(BudgetDetailsRepository budgetDetailsRepository) {
        this.budgetDetailsRepository = budgetDetailsRepository;
    }

    public List<BudgetDetails> findAll() {
        return budgetDetailsRepository.findAll();
    }

    public void save(BudgetDetails budgetDetails) {
        budgetDetailsRepository.save(budgetDetails);
    }

    public BudgetDetails findById(int id) {
        Optional<BudgetDetails> result = budgetDetailsRepository.findById(id);
        return result.orElse(null);  // Retourne `null` si aucun détail n'est trouvé
    }

    public List<BudgetDetails> findByBudgetId(int budgetId) {
        return budgetDetailsRepository.findByBudgetId(budgetId);
    }

    public void delete(BudgetDetails budgetDetails) {
        budgetDetailsRepository.delete(budgetDetails);
    }
}
