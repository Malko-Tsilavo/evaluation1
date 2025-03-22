package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "montant_restant")
    private Double montantRestant;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Relation avec la table `customer`

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

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
        if (this.montantRestant == null) {
            this.montantRestant = montant;
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(Double montantRestant) {
        this.montantRestant = montantRestant;
    }
}
