package com.example.dao;

import com.example.models.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            logger.error("Ошибка при получении пользователя с ID: " + id, e);
            return null;
        }
    }

    @Override
    public User save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                Long id = (Long) session.save(user);
                user.setId(id);
                tx.commit();
                return user;
            } catch (HibernateException e) {
                if (tx != null) tx.rollback();
                throw e;
            }
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.update(user);
                transaction.commit();
            } catch (HibernateException e) {
                if (transaction != null) transaction.rollback();
                logger.error("Ошибка при обновлении пользователя: " + user, e);
                throw e;
            }
        }
    }

    @Override
    public void delete(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.delete(user);
                transaction.commit();
                logger.info("Пользователь успешно удален: {}", user);
            } catch (HibernateException e) {
                if (transaction != null) transaction.rollback();
                logger.error("Ошибка при удалении пользователя: {}", user, e);
                throw new RuntimeException("Не удалось удалить пользователя", e);
            }
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            logger.error("Ошибка при получении списка пользователей", e);
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return (User) session.createQuery("FROM User WHERE email = :email")
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (HibernateException e) {
            logger.error("Ошибка при поиске пользователя по email: " + email, e);
            return null;
        }
    }
}
