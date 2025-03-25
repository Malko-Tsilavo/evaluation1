package site.easy.to.build.crm.entity;

import java.time.LocalDateTime;

public class DepenseDTO {
    private int idDepense;
    private double montant;
    private LocalDateTime dateDepense;
    private int ticketId;
    private String ticketSubject;
    private int budgetId;
    private String budgetNom;
    private Integer leadId;
    private String leadNom;
    private String clientNom;
    private int clientId; // Peut être null

    public int getBudgetId() {
        return budgetId;
    }

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public int getIdDepense() {
        return idDepense;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public double getMontant() {
        return montant;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    public void setIdDepense(int idDepense) {
        this.idDepense = idDepense;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getBudgetNom() {
        return budgetNom;
    }

    public int getClientId() {
        return clientId;
    }    

    public String getClientNom() {
        return clientNom;
    }

    public String getLeadNom() {
        return leadNom;
    }

    public String getTicketSubject() {
        return ticketSubject;
    }

    public void setBudgetNom(String budgetNom) {
        this.budgetNom = budgetNom;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public void setLeadNom(String leadNom) {
        this.leadNom = leadNom;
    }

    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }
    // Constructeurs, getters et setters
}
