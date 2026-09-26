package com.gestionformation.evaluation;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.session.Session;
@Entity
@Table(name = "evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private Double note;

    @Column(nullable = false)
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

}
