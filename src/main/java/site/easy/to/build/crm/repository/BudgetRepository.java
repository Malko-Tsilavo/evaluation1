package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.Budget;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    // Recherche d'un budget par son nom
    Optional<Budget> findByName(String name);

    // Recherche d'un budget par son id
    Optional<Budget> findById(int id);
}
