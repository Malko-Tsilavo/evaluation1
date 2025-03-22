package site.easy.to.build.crm.service.depense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.time.LocalDateTime;
import java.util.List;
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
}
