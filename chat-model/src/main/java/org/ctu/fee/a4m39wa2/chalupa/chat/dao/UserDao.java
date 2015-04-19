package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class UserDao extends AbstractDao<User> {

    @PersistenceContext
    private EntityManager em;

    public User findByUsername(String username) {
        return findByParams(new EqualParam<>("username", username));
    }

    @Override
    public void persist(User user) {
        user.getRoles().stream().forEach(r -> em.persist(r));
        super.persist(user);
    }
}
