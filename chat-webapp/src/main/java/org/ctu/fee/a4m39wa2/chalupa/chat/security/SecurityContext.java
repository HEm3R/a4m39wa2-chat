package org.ctu.fee.a4m39wa2.chalupa.chat.security;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;

import javax.enterprise.context.RequestScoped;

import lombok.Getter;
import lombok.Setter;

@RequestScoped
public class SecurityContext {

    @Getter @Setter private User user;
}
