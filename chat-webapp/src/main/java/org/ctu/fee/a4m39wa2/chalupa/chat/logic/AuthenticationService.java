package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.dao.UserDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.PasswordUtils;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class AuthenticationService {

    @Inject
    private UserDao dao;

    public User authenticate(String username, String password) {
        final User user = dao.findByUsername(username);
        if (user != null) {
            if (PasswordUtils.generatePasswordHash(password, username).equals(user.getPasswordHash())) {
                user.getRoles().size(); // load roles
                return user; // authenticated
            }
        }

        return null;
    }
}
