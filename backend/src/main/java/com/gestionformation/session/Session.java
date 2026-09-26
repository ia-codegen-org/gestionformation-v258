package com.gestionformation.session;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.formation.Formation;
import com.gestionformation.formateur.Formateur;
import com.gestionformation.salle.Salle;
import com.gestionformation.etudiant.Etudiant;
import java.util.List;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.evaluation.Evaluation;
import java.util.Date;
@Entity
@Table(name = "session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private Date dateDebut;

    @Column(nullable = false)
    private Date dateFin;


    // ✅ Champ calculé
    @Transient
    public Integer getNbEtudiants() {
        return etudiants != null ? etudiants.size() : 0;
    }

    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Formateur formateur;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToMany(mappedBy = "sessions")
    private List<Etudiant> etudiants;

    @OneToMany(mappedBy = "session")
    private List<Inscription> inscriptions;

    @OneToMany(mappedBy = "session")
    private List<Evaluation> evaluations;

}
