package org.idvairaz.mymod.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

/**
 * Управляет подключениями к БД и предоставляет доступ к репозиториям
 * Инициализирует Hibernate EntityManagerFactory и управляет жизненным циклом подключений
 */
public class DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);

    private static EntityManagerFactory entityManagerFactory;
    private static MessageRepository messageRepository;

    /**
     * Инициализирует подключение к базе данных и Hibernate EntityManagerFactory
     * Настраивает свойства подключения и создает экземпляры репозиториев
     */
    public static void initialize() {
        try {
            Properties properties = new Properties();
            properties.setProperty("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/minecraft_db");
            properties.setProperty("javax.persistence.jdbc.user", "mc_user");
            properties.setProperty("javax.persistence.jdbc.password", "mc_password");
            properties.setProperty("javax.persistence.jdbc.driver", "org.postgresql.Driver");

            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            properties.setProperty("hibernate.show_sql", "true");

            entityManagerFactory = Persistence.createEntityManagerFactory("minecraft-pu", properties);

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            messageRepository = new MessageRepository(entityManager);

            LOGGER.info("DatabaseManager initialized successfully!");

        } catch (Exception e) {
            LOGGER.error("Failed to initialize DatabaseManager", e);
        }
    }

    /**
     * Сохраняет сообщение в базе данных для указанного игрока
     * @param playerUuid UUID игрока, отправившего сообщение
     * @param text текст сообщения для сохранения
     */
    public static void saveMessage(UUID playerUuid, String text) {
        if (messageRepository == null) {
            LOGGER.error("MessageRepository not initialized!");
            return;
        }

        messageRepository.save(playerUuid, text);
    }

    /**
     * Закрывает подключения к базе данных и очищает ресурсы
     * Должен вызываться при остановке сервера
     */
    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            LOGGER.info("Database connections closed");
        }
    }
}
