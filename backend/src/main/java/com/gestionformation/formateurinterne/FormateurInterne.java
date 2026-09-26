package com.gestionformation.formateurinterne;

import jakarta.persistence.*;
import lombok.*;
import com.gestionformation.formateur.Formateur;
import lombok.experimental.SuperBuilder;
@Entity
@Table(name = "formateurinterne")
@DiscriminatorValue("FORMATEURINTERNE")
@PrimaryKeyJoinColumn
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class FormateurInterne extends Formateur {

    @Column(nullable = false)
    private Double salaireMensuel;

}
