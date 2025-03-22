package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "taux_alerte")
@Data
public class TauxAlerte {

    @Id
    @Column(name = "id_taux_alert")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTauxAlert;

    @Column(name = "taux", nullable = false)
    private Double taux;

    @Column(name = "date_declaration", nullable = false)
    private LocalDateTime dateDeclaration;
}
