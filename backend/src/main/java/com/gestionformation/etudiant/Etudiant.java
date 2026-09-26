package com.gestionformation.etudiant;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.session.Session;
import java.util.List;
import com.gestionformation.evaluation.Evaluation;
import com.gestionformation.certificat.Certificat;
import com.gestionformation.inscription.Inscription;
import java.util.Date;
import com.gestionformation.user.User;
import lombok.experimental.SuperBuilder;
@Entity
@Table(name = "etudiant")
@DiscriminatorValue("ETUDIANT")
@PrimaryKeyJoinColumn
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Etudiant extends User {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private Date dateNaissance;


    // ✅ Champ calculé
    @Transient
    public Double getNoteMoyenne() {
        if (evaluations == null || evaluations.isEmpty()) return 0.0; return evaluations.stream().filter(e -> e.getNote() != null).mapToDouble(e -> e.getNote()).average().orElse(0.0);
    }


    // ✅ Champ calculé
    @Transient
    public Integer getNbCertificats() {
        return certificats != null ? certificats.size() : 0;
    }

    @ManyToMany
    @JoinTable(name = "etudiant_session",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "session_id"))
    private List<Session> sessions;

    @OneToMany(mappedBy = "etudiant")
    private List<Evaluation> evaluations;

    @OneToMany(mappedBy = "etudiant")
    private List<Certificat> certificats;

    @OneToMany(mappedBy = "etudiant")
    private List<Inscription> inscriptions;

}
