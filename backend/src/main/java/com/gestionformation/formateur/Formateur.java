package com.gestionformation.formateur;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.departement.Departement;
import com.gestionformation.formation.Formation;
import java.util.List;
import com.gestionformation.session.Session;
import com.gestionformation.user.User;
import lombok.experimental.SuperBuilder;
@Entity
@Table(name = "formateur")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@DiscriminatorValue("FORMATEUR")
@PrimaryKeyJoinColumn
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Formateur extends User {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private Integer anneesExperience;


    // ✅ Champ calculé
    @Transient
    public Integer getNbSessions() {
        return sessions != null ? sessions.size() : 0;
    }

    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @ManyToMany
    @JoinTable(name = "formateur_formation",
        joinColumns = @JoinColumn(name = "formateur_id"),
        inverseJoinColumns = @JoinColumn(name = "formation_id"))
    private List<Formation> formations;

    @OneToMany(mappedBy = "formateur")
    private List<Session> sessions;

}
