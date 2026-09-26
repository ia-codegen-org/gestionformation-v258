package com.gestionformation.inscription;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.session.Session;
import com.gestionformation.paiement.Paiement;
import java.util.Date;
@Entity
@Table(name = "inscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InscriptionStatut statut;

    @Column(nullable = false)
    private Date dateInscription;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

}
