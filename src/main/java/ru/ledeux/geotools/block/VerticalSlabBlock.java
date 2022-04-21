package ru.ledeux.geotools.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

// Класс вертикальных полублоков с текстурой андезита
public class VerticalSlabBlock extends HorizontalFacingBlock {

    // Конструктор класса
    public VerticalSlabBlock(Settings settings) {
        super(settings);
        // Установка стандартного состояния блока путем вызова родительского метода класса Block
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    // Добавление нового свойства блоку
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    // Установка параметров отрисовки блока
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
            case SOUTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, 1.0f);
            case EAST:
                return VoxelShapes.cuboid(0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case WEST:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
            default:
                return VoxelShapes.fullCube();
        }
    }

    // Получение свойства блока в зависимости от направления установки
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

}