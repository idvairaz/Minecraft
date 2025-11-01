package org.idvairaz.mymod.server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.idvairaz.mymod.db.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный инициализатор мода на стороне сервера
 * Обрабатывает инициализацию БД и события жизненного цикла сервера
 */
public class MyModServer implements ModInitializer {
    public static final String MOD_ID = "mymod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Инициализирует серверную часть мода
     * Настраивает подключение к базе данных и обработчики остановки сервера
     */
    @Override
    public void onInitialize() {

        LOGGER.info("Hello Fabric world! from MyModServer");

        DatabaseManager.initialize();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            DatabaseManager.close();
        });
    }
}
