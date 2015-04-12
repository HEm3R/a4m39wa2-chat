package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.TwoValueObject;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.LinkedList;
import java.util.List;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    public User find(long id) {
        return em.find(User.class, id);
    }

    public User findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("select u from User u where u.username = :username", User.class);
        query.setParameter("username", username);
        query.setMaxResults(1);
        return getSingleResult(query.getResultList());
    }

    private User getSingleResult(List<User> users) {
        if (users.isEmpty()) {
            return null;
        }
        if (users.size() > 1) {
            throw new RuntimeException("Cannot retrieve single result");
        }
        return users.get(0);
    }

    public int countAll() {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        final Root<User> root = criteriaQuery.from(User.class);

        criteriaQuery.select(cb.count(root));

        final TypedQuery<Long> query = em.createQuery(criteriaQuery);

        return query.getSingleResult().intValue();
    }

    public List<User> findAll(Integer offset, Integer limit, List<TwoValueObject<String, OrderDirection>> orderBy) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        final Root<User> root = criteriaQuery.from(User.class);

        if (orderBy != null) {
            final List<Order> orders = new LinkedList<>();
            for (TwoValueObject<String, OrderDirection> entry : orderBy) {
                switch (entry.getSecond()) {
                    case ASC:
                        orders.add(cb.asc(root.get(entry.getFirst())));
                        break;
                    case DESC:
                        orders.add(cb.desc(root.get(entry.getFirst())));
                        break;
                }
            }
            criteriaQuery.orderBy(orders);
        } else {
            criteriaQuery.orderBy(cb.asc(root.get("id")));
        }

        final TypedQuery<User> query = em.createQuery(criteriaQuery);

        if (offset != null) {
            query.setFirstResult(offset);
        }
        if (limit != null) {
            query.setMaxResults(limit);
        }

        return query.getResultList();
    }

    public void persist(User user) {
        user.getRoles().stream().forEach(r -> em.persist(r));
        em.persist(user);
    }
}
