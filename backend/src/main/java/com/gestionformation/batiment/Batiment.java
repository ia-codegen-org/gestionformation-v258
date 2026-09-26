package com.gestionformation.batiment;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.salle.Salle;
import java.util.List;
@Entity
@Table(name = "batiment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @OneToMany(mappedBy = "batiment")
    private List<Salle> salles;

}
