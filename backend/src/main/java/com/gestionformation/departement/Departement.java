package com.gestionformation.departement;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.formateur.Formateur;
import java.util.List;
@Entity
@Table(name = "departement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Double budget;

    @OneToMany(mappedBy = "departement")
    private List<Formateur> formateurs;

}
