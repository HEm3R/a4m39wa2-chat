package org.ctu.fee.a4m39wa2.chalupa.chat.dao;

import org.ctu.fee.a4m39wa2.chalupa.chat.model.BaseEntity;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.ReflectionUtils;
import org.ctu.fee.a4m39wa2.chalupa.chat.utils.TwoValueObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

public class AbstractDao<T extends BaseEntity> {

    @Getter(lazy = true, value = AccessLevel.PROTECTED)
    private final Class<T> entityClass = ReflectionUtils.getGenericClass(getClass(), 0);

    @PersistenceContext
    private EntityManager em;

    public T find(long id) {
        return em.find(getEntityClass(), id);
    }

    public T findByParams(Param<T>... params) {
        return findByParams(Arrays.asList(params));
    }

    public T findByParams(List<Param<T>> params) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = cb.createQuery(getEntityClass());
        final Root<T> root = criteriaQuery.from(getEntityClass());

        List<Predicate> predicates = new LinkedList<>();
        params.forEach(p -> p.accept(predicates, cb, root));
        if (!predicates.isEmpty()) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        criteriaQuery.orderBy(cb.asc(root.get("id")));
        final TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setMaxResults(1);

        return DaoUtils.getSingleResult(query.getResultList());
    }

    public int countAll(Param<T>... params) {
        return countAll(Arrays.asList(params));
    }

    public int countAll(List<Param<T>> params) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        final Root<T> root = criteriaQuery.from(getEntityClass());

        criteriaQuery.select(cb.count(root));

        final List<Predicate> predicates = new LinkedList<>();
        params.forEach(p -> p.accept(predicates, cb, root));
        if (!predicates.isEmpty()) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        final TypedQuery<Long> query = em.createQuery(criteriaQuery);
        return query.getSingleResult().intValue();
    }

    public List<T> findAll(
            Integer offset, Integer limit,
            List<TwoValueObject<String, OrderDirection>> orderBy,
            List<Param<T>> params) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> criteriaQuery = cb.createQuery(getEntityClass());
        final Root<T> root = criteriaQuery.from(getEntityClass());

        List<Predicate> predicates = new LinkedList<>();
        params.forEach(p -> p.accept(predicates, cb, root));
        if (!predicates.isEmpty()) {
            criteriaQuery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        if (orderBy != null) {
            final List<Order> orders = new LinkedList<>();
            orderBy.forEach(entry -> {
                switch (entry.getSecond()) {
                    case ASC:
                        orders.add(cb.asc(root.get(entry.getFirst())));
                        break;
                    case DESC:
                        orders.add(cb.desc(root.get(entry.getFirst())));
                        break;
                }
            });
            criteriaQuery.orderBy(orders);
        } else {
            criteriaQuery.orderBy(cb.asc(root.get("id")));
        }

        final TypedQuery<T> query = em.createQuery(criteriaQuery);

        if (offset != null) {
            query.setFirstResult(offset);
        }
        if (limit != null) {
            query.setMaxResults(limit);
        }

        return query.getResultList();
    }

    public void persist(T entity) {
        em.persist(entity);
    }

    public void update(T entity) {
        em.merge(entity);
    }

    public void remove(long id) {
        T entity = find(id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}
