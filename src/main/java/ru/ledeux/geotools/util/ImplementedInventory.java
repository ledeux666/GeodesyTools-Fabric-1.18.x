package ru.ledeux.geotools.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * A simple {@code Inventory} implementation with only default methods + an item list getter.
 * Простая имплементация Inventory только с стандартными методами + геттер листа предметов.
 */
public interface ImplementedInventory extends Inventory {

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     * Извлечение листа предметов этого инвентаря.
     * Должен возращать тот же экземпляр каждый раз, когда вызывается метод.
     */
    DefaultedList<ItemStack> getItems();

    /**
     * Creates an inventory from the item list.
     * Создание инвентаря из листа предметов.
     */
    static ImplementedInventory of(DefaultedList<ItemStack> items) {
        return () -> items;
    }

    /**
     * Creates a new inventory with the specified size.
     * Создание нового инвентаря с определенным размером.
     */
    static ImplementedInventory ofSize(int size) {
        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    /**
     * Returns the inventory size.
     * Возврат размера инвентаря.
     */
    @Override
    default int size() {
        return getItems().size();
    }

    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     * Проверка инвентаря на пустоту.
     * @return true если инвентарь имеет только пустые ячейки, иначе false .
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the item in the slot.
     * Извлечение предмета из ячейки.
     */
    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are less items in the slot than what are requested,
     *              takes all items in that slot.
     * Вынимает предметы из ячейки инвентаря.
     * @param slot  Указывает на слот для удаления.
     * @param count Количество предметов, которые должны быть удалены. Если в ячейке имеется меньше предметов, чем в
     *              запросе, то вынимаются все предметы из слота.
     */
    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     * Вынимает все предметы из ячейки инвентаря.
     * @param slot Указывает на слот для удаления.
     */
    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     *              this inventory ({@link Inventory#getMaxCountPerStack()}),
     *              it gets resized to this inventory's maximum amount.
     * Замещает текущий стэк в инвентаре другим.
     * @param slot  Ячейка инвентаря, которая замещается новым стэком.
     * @param stack Новый стак предметов. Если размер стэка слишком большой для этого инвентаря,
     *              то его размер уменьшается под максимальное количество возможных предметов в стэке.
     */
    @Override
    default void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    /**
     * Clears the inventory.
     * Очистка инвентаря.
     */
    @Override
    default void clear() {
        getItems().clear();
    }

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     * Отмечает состояние как грязное.
     * Должен вызываться после изменений в инвентаре, чтобы игра могла правильно сохранять
     * содержимое инвентаря и уведомлять соседние блоки об изменениях инвентаря.
     */
    @Override
    default void markDirty() {
        // Override if you want behavior.
    }

    /**
     * @return true if the player can use the inventory, false otherwise.
     * @return true если игрок может использовать инвентарь, иначе false.
     */
    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
}
