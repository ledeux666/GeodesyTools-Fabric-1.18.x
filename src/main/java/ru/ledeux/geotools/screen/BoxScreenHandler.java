package ru.ledeux.geotools.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import ru.ledeux.geotools.GeoTools;

public class BoxScreenHandler extends ScreenHandler {

    private BlockPos pos;
    private final Inventory inventory;

    // Этот конструктор вызывается на клиенте, когда сервер хочет, чтобы был открыт ScreenHandler.
    // Клиент будет вызывать другие конструкторы с пустым Inventory и screenHandler будет автоматически
    // синхронизировать пустой инвентарь клиента с инвентарем на сервере.

    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(9));
        this.pos = buf.readBlockPos();
    }

    // Этот конструктор вызывается из BlockEntity на сервере без предварительного вызова прочих конструкторов,
    // сервер знает инвертарь контейнера и поэтому может напрямую предоставить его в качестве аргумента,
    // затем этот инвентарь будет синхронизирован с клиентом.
    public BoxScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {

        super(GeoTools.BOX_SCREEN_HANDLER, syncId);
        // Сравнение количество слотов инвентаря с ожидаемым значением.
        checkSize(inventory, 9);
        this.inventory = inventory;
        // Некоторые инвентари имеют собственную логику, когда игрок их открывает.
        // TODO: метод ничего не делает.
        inventory.onOpen(playerInventory.player);

        // Это поместит слот в правильные места для сетки 3x3. Слоты существуют как на сервере, так и на клиенте.
        // Это не будет отображать фон для слотов, т.к. это задача для Screens.
        int m;
        int l;
        // Инвентарь BlockEntity.
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 3; ++l) {
                this.addSlot(new Slot(inventory, l + m * 3, 62 + l * 18, 17 + m * 18));
            }
        }
        // Инвентарь игрока (без хотбара).
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // Хотбар игрока.
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    // Может ли игрок использовать... что?
    // TODO: метод ничего не делает.
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Позволяет взаимодействовать с инвентарем игрока с клавишей "Shift".
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {

        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {

            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {

                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }

            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }
}