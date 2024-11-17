package org.styly.efm.block;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.styly.efm.registries.ModMenus;

public class CrateMenu extends AbstractContainerMenu {
    private static final int SLOTS_PER_ROW = 9;
    private final Container container;
    private final int containerRows;

    private CrateMenu(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, int pRows,int pCols) {
        this(pType, pContainerId, pPlayerInventory,new SimpleContainer(pCols*pRows), pRows,pCols);
    }
    public static CrateMenu customCrate(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, int pRows,int pCols) {
        return new CrateMenu(pType, pContainerId, pPlayerInventory,new SimpleContainer(pCols*pRows), pRows,pCols);
    }
//    public static CrateMenu fourx3(int pContainerId, Inventory pPlayerInventory){
//        return customCrate(ModMenus.MY_MENU.get(), pContainerId,pPlayerInventory,4,3);
//    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
    public CrateMenu(MenuType<?> pType, int pContainerId, Inventory pPlayerInventory, Container pContainer, int pRows,int pCols) {
        super(pType, pContainerId);
        checkContainerSize(pContainer, pRows * pCols);
        this.container = pContainer;
        this.containerRows = pRows;
        pContainer.startOpen(pPlayerInventory.player);
        int i = (this.containerRows - 4) * 18;

        for (int j = 0; j < this.containerRows; j++) {
            for (int k = 0; k < pCols; k++) {
                this.addSlot(new Slot(pContainer, k + j * pCols, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.addSlot(new Slot(pPlayerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; i1++) {
            this.addSlot(new Slot(pPlayerInventory, i1, 8 + i1 * 18, 161 + i));
        }
    }
}
