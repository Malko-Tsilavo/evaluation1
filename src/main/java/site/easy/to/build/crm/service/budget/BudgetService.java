package site.easy.to.build.crm.service.budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    public void save(Budget budget) {
        System.out.println("Budget nom "+budget.getName());
        System.out.println("Budget valeur "+budget.getMontant());
        System.out.println("Budget Date et heure de creation "+budget.getDateCreation());
        budgetRepository.save(budget);
    }

    public Budget findByBudgetId(int id) {
        Optional<Budget> result = budgetRepository.findById(id);
        return result.orElse(null);  // Retourne null si aucun budget n'est trouvé
    }

    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }
}
