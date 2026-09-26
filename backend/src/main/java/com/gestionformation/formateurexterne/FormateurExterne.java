package com.gestionformation.formateurexterne;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.formateur.Formateur;
import lombok.experimental.SuperBuilder;
@Entity
@Table(name = "formateurexterne")
@DiscriminatorValue("FORMATEUREXTERNE")
@PrimaryKeyJoinColumn
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FormateurExterne extends Formateur {

    @Column(nullable = false)
    private Double tarifJournalier;

}
