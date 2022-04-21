package ru.ledeux.geotools.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.ledeux.geotools.block.entity.BoxBlockEntity;

public class BoxBlock extends BlockWithEntity {

    public BoxBlock(Settings settings) {
        super(settings);
    }

    // Связка Block с BlockEntity.
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BoxBlockEntity(pos, state);
    }

    // Определение типа рендера блока (по умолчанию return BlockRenderType.INVISIBLE).
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // Определение действия по нажатию ПКМ по блоку.
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {

        if (!world.isClient) {
            // Это вызовет createScreenHandlerFactory метод из BlockWithEntity, который вернет наш BlockEntity
            // приведенный к NamedScreenHandlerFactory.
            // Если ваш класс не расширяет BlockWithEntity, то необходимо реализовать createScreenHandlerFactory.
            // TODO: Почему метод createScreenHandlerFactory вызывается из BlockWithEntity, если в цепочке
            //  наследования BlockWithEntity и BlockState находятся в разных ветках.
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                // С помощью этого вызова сервер попросит клиента открыть соответствующий ScreenHandler.
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    // Этот метод выбрасывает все вещи на землю, когда блок ломается.
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {

        // Проверка старого и нового состояния.
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof BoxBlockEntity) {
                // Спавн предметов в мире, содержащихся в BlockEntity.
                ItemScatterer.spawn(world, pos, (BoxBlockEntity)blockEntity);
                // Обновление компараторов
                world.updateComparators(pos,this);
            }
            // Вызов родительского метода, который сравнивает старое и новое состояние. Если он они неравны, то
            // удаляет сам BlockEntity.
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    // Имеет ли блок выходной сигнал для компаратора?
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    // Получение целочисленного значения сигнала компаратора в зависимости от наполнения инвентаря.
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
