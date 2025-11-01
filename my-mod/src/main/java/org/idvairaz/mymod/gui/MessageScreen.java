package org.idvairaz.mymod.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.idvairaz.mymod.network.NetworkHandler;

/**
 * Графический интерфейс для отправки сообщений на сервер
 * Предоставляет поле ввода текста и кнопки отправки/закрытия
 */
public class MessageScreen extends Screen {
    private TextFieldWidget textField;

    /**
     * Создает новый MessageScreen
     */
    public MessageScreen() {
        super(Text.literal(""));
    }

    /**
     * Инициализирует компоненты экрана
     * Настраивает текстовое поле, кнопку отправки и кнопку закрытия
     */
    @Override
    protected void init() {
        super.init();

        int x = 50;
        int y = 60;
        int width = 200;
        int height = 20;

        this.textField = new TextFieldWidget(
                this.textRenderer,
                x, y, width, height,
                Text.literal("")
        );
        this.textField.setMaxLength(256);
        this.addSelectableChild(this.textField);
        this.setInitialFocus(this.textField);

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Send"),
                button -> this.onSend()
        ).dimensions(x, y + 30, width, height).build());

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> this.close()
        ).dimensions(x, y + 60, width, height).build());

    }

    /**
     * Отправляет  не пустое сообщение на сервер и закрывает окно
     * Вызывается при нажатии кнопки Send или клавиши Enter
     */
    private void onSend() {
        String message = this.textField.getText();
        if (!message.trim().isEmpty()) {
            NetworkHandler.sendMessageToServer(message);
        }
        this.close();
    }

    /**
     * Отрисовывает  интерфейс окна сообщений
     * Вызывается  много раз в секунду для плавного отображения
     *
     * @param context холст для рисования - через него рисуем текст, кнопки и другие элементы
     * @param mouseX координата X курсора мыши на экране (нужна для подсветки кнопок при наведении)
     * @param mouseY координата Y курсора мыши на экране (нужна для подсветки кнопок при наведении)
     * @param delta время в секундах с последнего обновления экрана (для плавной анимации)
     */
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                30,
                0xFFFFFF
        );

        this.textField.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }

    /**
     * Обрабатывает нажатия клавиш в окне
     * Enter отправляет сообщение, Escape закрывает окно, остальные клавиши обрабатывает родительский класс
     * @param keyCode код нажатой клавиши (257 = Enter, 256 = Escape)
     * @param scanCode технический код клавиши (зависит от клавиатуры)
     * @param modifiers модификаторы (Shift, Ctrl, Alt)
     * @return true если клавиша обработана, false если передать обработку дальше
     */
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.textField.isFocused()) {
            this.onSend();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}


