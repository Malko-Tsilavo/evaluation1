package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "depense")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_depense")
    private int idDepense;

    @Column(name = "montant")
    private double montant;

    @Column(name = "date_depense")
    private LocalDateTime dateDepense;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = true)  // ticket_id peut être null
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = true)
    private Lead lead;


    // Constructeur par défaut
    public Depense() {
    }

    // Constructeur avec tous les paramètres
    public Depense(double montant, LocalDateTime dateDepense, Ticket ticket, Budget budget, Lead lead) {
        this.montant = montant;
        this.dateDepense = dateDepense;
        this.ticket = ticket;
        this.budget = budget;
        this.lead = lead;
    }

    // Getters et setters
    public int getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }
}
