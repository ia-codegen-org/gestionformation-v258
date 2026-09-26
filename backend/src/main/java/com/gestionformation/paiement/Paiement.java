package com.gestionformation.paiement;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.facture.Facture;
import java.util.Date;
@Entity
@Table(name = "paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private Double montant;

    @Column(nullable = false)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaiementModePaiement modePaiement;

    @OneToOne(mappedBy = "paiement")
    private Inscription inscription;

    @OneToOne(mappedBy = "paiement")
    private Facture facture;

}
