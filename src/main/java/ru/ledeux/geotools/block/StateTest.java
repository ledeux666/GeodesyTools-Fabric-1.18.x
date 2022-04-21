package ru.ledeux.geotools.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// Класс тахеометра, расширяющий функционал родительского класса Block
public class StateTest extends Block {

    // Наследование родительского конструктора класса
    public StateTest(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LAUNCHED, false));
    }

    // Добавление нового свойства блоку (Запущен)
    public static final BooleanProperty LAUNCHED = BooleanProperty.of("launched");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(LAUNCHED);
    }

    // Смена состояния LAUNCHED на True при нажатии ПКМ по нему
    // Данный метод устарел, в новом не используется BlockState и BlockPos
    // TODO: Исключить устаревший метод
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        // Воспроизведение звука при смене состояния
        player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
        world.setBlockState(pos, state.with(LAUNCHED, true));
        return ActionResult.SUCCESS;
    }

    // Использование свойства LAUNCHED для вызова молнии при вставании игрока на блок
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (world.getBlockState(pos).get(LAUNCHED)){
            LightningEntity lightningEntity = (LightningEntity) EntityType.LIGHTNING_BOLT.create(world);
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
            world.spawnEntity(lightningEntity);
        }

        // Смена состояние после вставания на блок
        world.setBlockState(pos, state.with(LAUNCHED, false));
    }

    // TODO: Координаты связаны с классами по типу BlockPos
    // TODO: Сделать блок поворачиваемым, на подобии таблички (класс SignBlock)
}
