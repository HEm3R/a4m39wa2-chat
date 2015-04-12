package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.UserDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions.UniqueConstraintViolationException;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Role;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.BusinessRole;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.PasswordUtils;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class UserCrud {

    @Inject
    private UserDao dao;

    public User find(long id) {
        return dao.find(id);
    }

    public List<User> findAll(SelectionContext selectionContext) {
        return dao.findAll(selectionContext.getOffset(), selectionContext.getLimit(), selectionContext.getOrderBy());
    }

    public User create(String username, String password) throws UniqueConstraintViolationException {
        if (!isUnique(username)) {
            throw new UniqueConstraintViolationException("username");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtils.generatePasswordHash(password, username));

        user.getRoles().add(new Role(BusinessRole.MEMBER));
        if (dao.countAll() == 0) { // first user is admin
            user.getRoles().add(new Role(BusinessRole.ADMIN));
        }

        dao.persist(user);
        return user;
    }

    private boolean isUnique(String username) {
        return dao.findByUsername(username) == null;
    }
}