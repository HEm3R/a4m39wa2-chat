package org.ctu.fee.a4m39wa2.chalupa.chat.model;

import org.ctu.fee.a4m39wa2.chalupa.chat.access.BusinessRole;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="\"User\"")
@EqualsAndHashCode(of = "id", callSuper = false)
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter private Long id;

    @NotEmpty
    @Length(max = 60)
    @Column(unique = true)
    @Getter @Setter private String username;

    @NotNull
    @Getter @Setter private String passwordHash;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Getter @Setter private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Getter @Setter List<Message> messages;

    public List<BusinessRole> getBusinessRoles() {
        return getRoles().stream().map(r -> r.getRole()).collect(Collectors.toList());
    }
}
