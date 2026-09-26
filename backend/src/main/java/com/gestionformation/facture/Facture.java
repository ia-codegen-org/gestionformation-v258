package com.gestionformation.facture;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.paiement.Paiement;
import java.util.Date;
@Entity
@Table(name = "facture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Double montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FactureStatut statut;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

}
