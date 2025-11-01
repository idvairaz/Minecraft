package org.idvairaz.mymod.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.idvairaz.mymod.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


/**
 * Обрабатывает сетевое взаимодействие между клиентом и сервером
 * Регистрирует обработчики пакетов и управляет отправкой сообщений
 */
public class NetworkHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkHandler.class);


    /**
     * Регистрирует обработчики пакетов на стороне сервера
     * Настраивает прием сообщений TestDataPayload и сохраняет их в базу данных
     */
    public static void registerServer() {
        PayloadTypeRegistry.playC2S().register(TestDataPayload.ID, TestDataPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TestDataPayload.ID, TestDataPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TestDataPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                String messageText = payload.data();
                UUID playerUuid = context.player().getUuid();

                LOGGER.info("SERVER GET PACKET {} from player: {}", messageText, playerUuid);


                System.out.println("SERVER GET PACKET " + messageText + " playerUuid " + playerUuid);

                DatabaseManager.saveMessage(playerUuid, messageText);
            });
        });
    }

    /**
     * Отправляет сообщение с клиента на сервер
     * @param text текст сообщения для отправки
     */
    public static void sendMessageToServer(String text) {
        ClientPlayNetworking.send(new TestDataPayload(text));

    }
}
