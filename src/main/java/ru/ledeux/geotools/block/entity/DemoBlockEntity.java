package ru.ledeux.geotools.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import ru.ledeux.geotools.GeoTools;
import ru.ledeux.geotools.util.ImplementedInventory;

public class DemoBlockEntity extends BlockEntity implements ImplementedInventory, SidedInventory {

    public DemoBlockEntity(BlockPos pos, BlockState state) {
        super(GeoTools.DEMO_BLOCK_ENTITY, pos, state);
    }

    // Десериализация (чтение) параметров у BlockEntity.
    @Override
    public void readNbt(NbtCompound nbt) {

        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    // Сериализация (запись) параметров у BlockEntity.
    @Override
    protected void writeNbt(NbtCompound nbt) {

        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
    }

    // Определение действий BlockEntity в зависимости от внутреннего времени Minecraft.
    public static void tick(World world, BlockPos pos, BlockState state, DemoBlockEntity be) {
        // Пока что пусто.
    }

    /**
     * Реализация имплементрированных методов из интерфеса ImplementedInventory.
     * Все методы наследуют такой же функционал, что и в интерфейсе.
     */
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return ImplementedInventory.super.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return ImplementedInventory.super.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        return ImplementedInventory.super.removeStack(slot, count);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return ImplementedInventory.super.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
    }

    @Override
    public void clear() {
        ImplementedInventory.super.clear();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return ImplementedInventory.super.canPlayerUse(player);
    }

    /**
     * Реализация имплементрированных методов из интерфеса SidedInventory.
     */
    @Override
    public int[] getAvailableSlots(Direction side) {
        // Возвращаем массив для всех слотов инвентаря.
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        // Запрет на подачу предметов сверху.
        return dir != Direction.UP;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}