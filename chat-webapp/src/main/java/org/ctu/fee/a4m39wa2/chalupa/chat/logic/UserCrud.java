package org.ctu.fee.a4m39wa2.chalupa.chat.logic;

import org.ctu.fee.a4m39wa2.chalupa.chat.api.SelectionContext;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.EqualParam;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.Param;
import org.ctu.fee.a4m39wa2.chalupa.chat.dao.UserDao;
import org.ctu.fee.a4m39wa2.chalupa.chat.logic.exceptions.UniqueConstraintViolationException;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.Role;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.access.BusinessRole;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.PasswordUtils;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class UserCrud  {

    @Inject
    private UserDao dao;

    public User find(long id) {
        return find(id, false);
    }

    public User find(long id, boolean fetchRoles) {
        User user =  dao.find(id);
        if (user != null && fetchRoles) {
            user.getRoles().size();
        }
        return user;
    }

    public List<User> findAll(SelectionContext selectionContext) {

        List<Param<User>> where = new ArrayList<>(selectionContext.getWhere().size());
        selectionContext.getWhere().forEach(w -> where.add(new EqualParam<>(w.getFirst(), w.getSecond())));

        int count = dao.countAll(where);
        selectionContext.setTotal(count);
        if (count == 0) {
            return Collections.emptyList();
        }

        return dao.findAll(
                selectionContext.getOffset(),
                selectionContext.getLimit(),
                selectionContext.getOrderBy(),
                where
        );
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

    public void changePassword(User user, String password) {
        user.setPasswordHash(PasswordUtils.generatePasswordHash(password, user.getUsername()));
        dao.update(user);
    }

    private boolean isUnique(String username) {
        return dao.findByUsername(username) == null;
    }
}
