package ru.ledeux.geotools.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.ledeux.geotools.GeoTools;
import ru.ledeux.geotools.block.entity.DemoBlockEntity;

public class DemoBlock extends BlockWithEntity {

    public DemoBlock(Settings settings) {
        super(settings);
    }

    // Связка Block с BlockEntity.
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DemoBlockEntity(pos, state);
    }

    // Определение типа рендера блока.
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // При наследовании от BlockWithEntity по умолчанию используется значение INVISIBLE, поэтому нужно это изменить!
        return BlockRenderType.MODEL;
    }

    // Связка блока с внутренним счетчиком тиков Minecraft.
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, GeoTools.DEMO_BLOCK_ENTITY, DemoBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand,
                              BlockHitResult blockHitResult) {
        // TODO: isClient метод или переменная?
        if (world.isClient) return ActionResult.SUCCESS;
        Inventory blockEntity = (Inventory) world.getBlockEntity(blockPos);

        // Проверка: Пуста ли рука игрока?
        if (!player.getStackInHand(hand).isEmpty()) {
            // Проверка: Пуста ли первая (0) ячейка в инвентаре блока?
            if (blockEntity.getStack(0).isEmpty()) {
                // Скопировать стэк блоков в руке игрока в первую ячейку инвентаря блока.
                blockEntity.setStack(0, player.getStackInHand(hand).copy());
                // Удалить стэк блоков из руки игрока.
                player.getStackInHand(hand).setCount(0);
            } else if (blockEntity.getStack(1).isEmpty()) {
                blockEntity.setStack(1, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);
            } else {
                // Если инвентарь полон, то выводим следующее сообщение.
                System.out.println("The first slot holds "
                        + blockEntity.getStack(0) + " and the second slot holds " + blockEntity.getStack(1));
            }
        } else {
            // Если игрок ничего не держит в руке, то ему будет выдаваться стэки предметов из инвентаря.
            // Поиск первого слота инвентаря в котором что-то есть и выдача его игроку.
            if (!blockEntity.getStack(1).isEmpty()) {
                // Дать игроку стэк предметов из конечного слота (1).
                player.getInventory().offerOrDrop(blockEntity.getStack(1));
                // Удаление стэка из инвентаря блока.
                blockEntity.removeStack(1);
            } else if (!blockEntity.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(blockEntity.getStack(0));
                blockEntity.removeStack(0);
            }
        }
        return ActionResult.SUCCESS;
    }
}
