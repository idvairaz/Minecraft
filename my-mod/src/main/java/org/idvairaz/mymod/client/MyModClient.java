package org.idvairaz.mymod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.idvairaz.mymod.gui.MessageScreen;
import org.idvairaz.mymod.network.NetworkHandler;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный инициализатор мода на стороне клиента
 * Обрабатывает привязки клавиш и инициализацию графического интерфейса
 */
public class MyModClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("MyModClient");
    private static KeyBinding openGuiKey;

    /**
     * Инициализирует клиентскую часть мода
     * Настраивает привязки клавиш и сетевые обработчики
     */
    @Override
    public void onInitializeClient() {

        NetworkHandler.registerServer();

        LOGGER.info("MyModClient initializing...");

        try {
            openGuiKey = new KeyBinding(
                    "key.mymod.open_gui",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_Z,
                    "category.mymod.general"
            );

            KeyBindingHelper.registerKeyBinding(openGuiKey);
            LOGGER.info("✅ KeyBinding registered for Z key");

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (openGuiKey.wasPressed()) {
                    LOGGER.info("Z key pressed - attempting to open MessageScreen");
                    try {
                        if (client.currentScreen == null) {
                            client.setScreen(new MessageScreen());
                            LOGGER.info("MessageScreen opened successfully");
                        }
                    } catch (Exception e) {
                        LOGGER.error("Failed to open MessageScreen: {}", e.getMessage(), e);
                    }
                }
            });

            LOGGER.info("MyModClient initialized successfully");
        } catch (Exception e) {
            LOGGER.error("MyModClient initialization failed: {}", e.getMessage(), e);
        }
    }
}