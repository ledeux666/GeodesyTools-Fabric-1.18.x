package ru.ledeux.geotools.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

// Класс, расширяющий функционал родительского класса Item
public class Ruby extends Item {

    // Наследование родительского конструктора класса
    public Ruby(Settings settings) {
        super(settings);
    }

    // При нажатии ПКМ с предметом в руке будет проигрываться звук ломания блока шерсти
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
