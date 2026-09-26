package com.gestionformation.salle;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.batiment.Batiment;
import com.gestionformation.session.Session;
import java.util.List;
@Entity
@Table(name = "salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private Integer capaciteMax;

    @Column(nullable = false)
    private String equipement;

    @ManyToOne
    @JoinColumn(name = "batiment_id")
    private Batiment batiment;

    @OneToMany(mappedBy = "salle")
    private List<Session> sessions;

}
