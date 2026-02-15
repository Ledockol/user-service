package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry serviceRegistry;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.configure();

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties());
                serviceRegistry = builder.build();

                MetadataSources sources = new MetadataSources(serviceRegistry);
                Metadata metadata = sources.getMetadataBuilder().build();
                sessionFactory = metadata.buildSessionFactory();

            } catch (Exception e) {
                if (serviceRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(serviceRegistry);
                }
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
        }
    }
}
