package com.gestionformation.formation;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.categorie.Categorie;
import com.gestionformation.formateur.Formateur;
import java.util.List;
import com.gestionformation.session.Session;
@Entity
@Table(name = "formation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer dureeHeures;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToMany(mappedBy = "formations")
    private List<Formateur> formateurs;

    @OneToMany(mappedBy = "formation")
    private List<Session> sessions;

}
