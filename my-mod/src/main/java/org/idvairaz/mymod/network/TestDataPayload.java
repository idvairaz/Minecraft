package org.idvairaz.mymod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.idvairaz.protobuf.MessageOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Пользовательский payload для отправки сообщений по сети с использованием Protobuf кодирования
 * Реализует интерфейс CustomPayload Minecraft для Fabric networking
 */
public record TestDataPayload(String data) implements CustomPayload {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataPayload.class);

    public static final Identifier PAYLOAD_ID = Identifier.of("mymod", "message");
    public static final Id<TestDataPayload> ID = new Id<>(PAYLOAD_ID);

    public static final PacketCodec<PacketByteBuf, TestDataPayload> CODEC =
            CustomPayload.codecOf(TestDataPayload::write, TestDataPayload::read);

    /**
     * Записывает данные payload в буфер с использованием Protobuf кодирования
     * @param buf буфер пакета для записи
     */
    private void write(PacketByteBuf buf) {
        try {
            MessageOuterClass.Message message =
                    MessageOuterClass.Message.newBuilder()
                            .setText(data)
                            .build();

            byte[] protobufData = message.toByteArray();
            buf.writeByteArray(protobufData);

            LOGGER.info("Encoded Protobuf: {} -> {}", data, Arrays.toString(protobufData));
        } catch (Exception e) {
            LOGGER.error("Protobuf encode error: {}", e.getMessage());
        }
    }

    /**
     * Читает данные payload из буфера с использованием Protobuf декодирования
     * @param buf буфер пакета для чтения
     * @return декодированный экземпляр TestDataPayload
     */
    private static TestDataPayload read(PacketByteBuf buf) {
        try {
            byte[] protobufData = buf.readByteArray();
            MessageOuterClass.Message message =
                    MessageOuterClass.Message.parseFrom(protobufData);

            String data = message.getText();
            LOGGER.info("Decoded Protobuf: {} -> {}", Arrays.toString(protobufData), data);
            return new TestDataPayload(data);

        } catch (Exception e) {
            LOGGER.error("Protobuf decode error: {}", e.getMessage());
            return new TestDataPayload("error");
        }
    }

    /**
     * @return идентификатор payload
     */
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
