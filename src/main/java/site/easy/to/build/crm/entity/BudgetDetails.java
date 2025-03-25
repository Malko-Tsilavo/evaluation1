package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "budget-details")
public class BudgetDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_details_id")
    private int id;

    @Column(name = "montant")
    private Double montant;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;  // Relation avec la table `budget`

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
