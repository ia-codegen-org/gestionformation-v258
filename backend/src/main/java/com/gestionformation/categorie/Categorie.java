package com.gestionformation.categorie;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.formation.Formation;
import java.util.List;
@Entity
@Table(name = "categorie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "categorie")
    private List<Formation> formations;

}
