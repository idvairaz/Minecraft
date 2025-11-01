package org.idvairaz.mymod.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * "JPA Repository" как паттерн для работы с сообщениями
 * Реализует сохранение в БД без Spring
 */
public class MessageRepository {
    private final EntityManager entityManager;
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);


    /**
     * Конструктор класса
     */
    public MessageRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Создает новый MessageEntity и сохраняет его в БД
     * @param playerUuid UUID игрока, отправившего сообщение
     * @param text текст сообщения для сохранения
     */
    public void save(UUID playerUuid, String text) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            MessageEntity message = new MessageEntity(playerUuid, text);
            entityManager.persist(message);

            transaction.commit();
            LOGGER.info("Message saved in DB. Player: {}, Text: {}", playerUuid, text);

        } catch (Exception e) {
            LOGGER.error("Failed to save message in DB. Player: {}, Text: {}", playerUuid, text, e);
        }
    }
}
