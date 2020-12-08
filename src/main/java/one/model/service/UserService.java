package one.model.service;


import one.model.entity.User;

import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Stateful
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 20)
@Singleton
public class UserService {

    @PersistenceContext(unitName = "UTILITIES")
    public EntityManager em;

    public User findByEmail(String email) {
        String queryString = "SELECT u FROM User u " +
                "WHERE LOWER(u.email) = :email";
        Query query = em.createQuery(queryString);

        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    public List<User> findAllUsers() {
        String queryString = "SELECT u FROM User u";
        Query query = em.createQuery(queryString);
        return query.getResultList();
    }

    public List<User> findAllUsersByName(String name) {
        String queryString = "SELECT u FROM User u WHERE LOWER(u.name) = :name";
        Query query = em.createQuery(queryString);
        query.setParameter("name", name);
        return query.getResultList();
    }


    public User addUser(User user) {
        em.getTransaction().begin();
        User userFromDB = em.merge(user);
        em.getTransaction().commit();
        return userFromDB;
    }

    @Remove
    public void deleteUser(long id) {
        em.getTransaction().begin();
        em.remove(getUser(id));
        em.getTransaction().commit();
    }

    public User getUser(long id) {
        return em.find(User.class, id);
    }

    public void updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }
}