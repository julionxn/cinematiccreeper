package me.julionxn.cinematiccreeper.managers.presets;

import me.julionxn.cinematiccreeper.CinematicCreeper;
import me.julionxn.cinematiccreeper.entity.NpcEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiConsumer;

public class PresetOptionsHandlers {

    public static void addToBuf(PacketByteBuf buf, PresetOptions presetOptions) {
        processFields((valueType, field) -> {
            try {
                Object value = field.get(presetOptions);
                switch (valueType) {
                    case STRING -> buf.writeString((String) value);
                    case BOOLEAN -> buf.writeBoolean((Boolean) value);
                }
            } catch (IllegalAccessException e) {
                CinematicCreeper.LOGGER.error("Error adding to buf.", e);
            }
        });
    }

    public static PresetOptions fromBuf(PacketByteBuf buf) {
        PresetOptions presetOptions = new PresetOptions();
        processFields((valueType, field) -> {
            try {
                switch (valueType) {
                    case STRING -> field.set(presetOptions, buf.readString());
                    case BOOLEAN -> field.set(presetOptions, buf.readBoolean());
                }
            } catch (IllegalAccessException e) {
                CinematicCreeper.LOGGER.error("Error reading from buf.", e);
            }
        });
        return presetOptions;
    }

    private static void processFields(BiConsumer<ValueType, Field> valueTypeConsumer) {
        Field[] fields = PresetOptions.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Optional<ValueType> valueTypeOptional = getValueType(field.getType());
            if (valueTypeOptional.isEmpty()) continue;
            valueTypeConsumer.accept(valueTypeOptional.get(), field);
        }
    }

    private static Optional<ValueType> getValueType(Class<?> clazz) {
        String nameType = clazz.getSimpleName().toUpperCase();
        try {
            return Optional.of(ValueType.valueOf(nameType));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static PresetOptions fromEntity(Entity entity) {
        PresetOptions presetOptions = new PresetOptions();
        presetOptions.displayName = entity.getDisplayName().getString();
        presetOptions.showDisplayName = entity.isCustomNameVisible();
        presetOptions.sneaking = entity.isSneaking();
        if (entity instanceof MobEntity mobEntity) {
            presetOptions.holdingItem = Registries.ITEM.getId(mobEntity.getMainHandStack().getItem()).toString();
        }
        if (entity instanceof NpcEntity) {
            presetOptions.skinUrl = ((NpcEntity) entity).getSkinUrl();
        }
        return presetOptions;
    }

    public static void applyPresetOptions(Entity entity, PresetOptions presetOptions) {
        entity.setCustomNameVisible(presetOptions.showDisplayName);
        entity.setCustomName(Text.of(presetOptions.displayName));
        entity.setSneaking(presetOptions.sneaking);
        if (entity instanceof MobEntity mobEntity) {
            setStackInHand(mobEntity, presetOptions.holdingItem);
        }
    }

    private static void setStackInHand(MobEntity mobEntity, String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier == null) return;
        Item item = Registries.ITEM.get(identifier);
        mobEntity.setStackInHand(Hand.MAIN_HAND, new ItemStack(item));
    }

    private enum ValueType {
        STRING, BOOLEAN
    }

}
