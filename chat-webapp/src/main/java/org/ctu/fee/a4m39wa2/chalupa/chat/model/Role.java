package org.ctu.fee.a4m39wa2.chalupa.chat.model;

import org.ctu.fee.a4m39wa2.chalupa.chat.security.BusinessRole;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public Role(BusinessRole role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Getter @Setter private BusinessRole role;
}
