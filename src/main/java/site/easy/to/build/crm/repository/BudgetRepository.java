package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.easy.to.build.crm.entity.Budget;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    // Recherche d'un budget par son id
    Optional<Budget> findById(int id);

    @Query("SELECT b FROM Budget b JOIN FETCH b.customer WHERE b.customer.customerId = :id")
    List<Budget> findByCustomer(@Param("id") int id);
    
}
