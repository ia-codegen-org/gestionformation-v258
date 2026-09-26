package com.gestionformation.certificat;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.etudiant.Etudiant;
import java.util.Date;
@Entity
@Table(name = "certificat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private String createdBy;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private Date dateObtention;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificatMention mention;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

}
