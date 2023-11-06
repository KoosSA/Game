package client.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import com.koossa.logger.Log;

import client.gui.INiftyRestartEventSubscriber;
import client.io.KeyBinds;
import client.io.input.Input;
import client.io.input.InputStates;
import client.io.input.receivers.handlers.IInputHandler;
import client.utils.Globals;
import de.lessvoid.nifty.Nifty;

/**
 * The Class SlotInventory.
 */
public class SlotInventory implements IInputHandler, INiftyRestartEventSubscriber {

	private List<InvSlotData> slots = new ArrayList<InvSlotData>();
	private List<Integer> tempList = new ArrayList<Integer>();
	private boolean visible = false;
	private InventoryController screenController;

	public SlotInventory(int invSize, Nifty nifty, String iconFolderPath) {
		registerInputHandler(InputStates.GAME);
		registerInputHandler(InputStates.GUI);
		registerRestartEventSubscriber();
		for (int i = 0; i < invSize; i++) {
			slots.add(new InvSlotData());
		}
		Globals.ngui.loadXML("inv.xml");

	}

	private void updateInvGui() {
		screenController = (InventoryController) Globals.ngui.nifty.getScreen("inv").getScreenController();
		screenController.updateGui(slots);
	}

	private void createSlots() {
		screenController = (InventoryController) Globals.ngui.nifty.getScreen("inv").getScreenController();
		screenController.createSlots(slots);
	}

	/**
	 * Gets a empty slot in the inventory.
	 *
	 * @return the empty slot index or -1 if no empty slot was found
	 */
	private int getEmptySlot() {
		Log.debug(this, "Getting empty slot.");
		for (int index = 0; index < slots.size(); index++) {
			if (slots.get(index).getItem() == null || slots.get(index).getItemCount() <= 0) {
				Log.debug(this, "Empty slot found @: " + index);
				return index;
			}
		}
		Log.debug(this, "No empty slot found.");
		return -1;
	}

	private int getAmountOfItemAvailable(InvItem item) {
		tempList = getSlotsWithItem(item);
		int available = 0;
		for (int i = 0; i < tempList.size(); i++) {
			available += slots.get(tempList.get(i)).getItemCount();
		}
		return available;
	}

	private int getAmountOfItemAvailable(List<Integer> listOfIndicesContainingItem) {
		int available = 0;
		for (int i = 0; i < listOfIndicesContainingItem.size(); i++) {
			available += slots.get(listOfIndicesContainingItem.get(i)).getItemCount();
		}
		return available;
	}

	/**
	 * Gets a list of slots with specified item.
	 *
	 * @param item the item
	 * @return the slots with item
	 */
	private List<Integer> getSlotsWithItem(InvItem item) {
		tempList.clear();
		Log.debug(this, "Getting slots containing item: " + item.getItemName());
		for (int index = 0; index < slots.size(); index++) {
			if (item == slots.get(index).getItem()) {
				tempList.add(index);
			}
		}
		Log.debug(this, "Slots containing item: " + tempList);
		return tempList;
	}

	/**
	 * Gets the slot with space for specified item.
	 *
	 * @param item the item
	 * @return the slot index with space or -1 if none were found
	 */
	private int getSlotWithSpace(InvItem item) {
		Log.debug(this, "Getting sloat with space for item: " + item.getItemName());
		if (!item.isStackable()) {
			Log.debug(this, "Item not stackable -- Getting empty slot");
			return getEmptySlot();
		}
		tempList = getSlotsWithItem(item);
		if (tempList.size() <= 0) {
			Log.debug(this, "No slots with item found, getting empty slot");
			return getEmptySlot();
		} else {
			for (int i = 0; i < tempList.size(); i++) {
				int index = tempList.get(i);
				if (slots.get(index).getItemCount() < item.getMaxStackSize()) {
					Log.debug(this, "Slot with space found @: " + index);
					return index;
				}
			}
		}
		Log.debug(this, "No slot with space found, inventory probably full");
		return getEmptySlot();
	}

	/**
	 * Adds the item to inventory if space is found.
	 *
	 * @param itemToAdd   the item to add
	 * @param amountToAdd the amount to add
	 * @return the amount that could not be added
	 */
	public int addItemToInventory(InvItem itemToAdd, int amountToAdd) {
		Log.debug(this, "Adding item to inventory: " + itemToAdd.getItemName() + " x" + amountToAdd);
		int slotIndex = getSlotWithSpace(itemToAdd);
		if (slotIndex == -1) {
			Log.debug(this, "No space in inventory to add item: " + itemToAdd.getItemName() + " x" + amountToAdd);
			return amountToAdd;
		} else {
			InvSlotData slot = slots.get(slotIndex);
			if (slot.getItem() == null) {
				slot.setItem(itemToAdd);
			}
			int space = itemToAdd.getMaxStackSize() - slot.getItemCount();
			Log.debug(this, "Space on slot " + slotIndex + " = " + space);
			if (space <= amountToAdd) {
				int nata = amountToAdd - space;
				slots.get(slotIndex).setItemCount(itemToAdd.getMaxStackSize());
				Log.debug(this, "Adding to inventory: " + itemToAdd.getItemName() + " x"
						+ (amountToAdd - itemToAdd.getMaxStackSize()) + " @ slot: " + slotIndex);
				updateInvGui();
				return addItemToInventory(itemToAdd, nata);
			} else {
				slots.get(slotIndex).setItemCount(slot.getItemCount() + amountToAdd);
				Log.debug(this, "Added to inventory: " + itemToAdd.getItemName() + " x" + amountToAdd + " @ slot: "
						+ slotIndex);
				updateInvGui();
				return 0;
			}
		}
	}

	public boolean removeItemFromInventory(InvItem item, int amountToRemove) {
		
		tempList = getSlotsWithItem(item);
		if (getAmountOfItemAvailable(tempList) < amountToRemove) {
			Log.debug(this, "Item not found in inventory or not in suficient quntities to remove: " + item.getItemName());
			return false;
		}
		InvSlotData slot = slots.get(tempList.get(0));
		int onSlot = slot.getItemCount();
		if (onSlot >= amountToRemove) {
			onSlot = onSlot - amountToRemove;
			amountToRemove = 0;
			slot.setItemCount(onSlot);
			if (onSlot <= 0) {
				slot.setItem(null);
			}
			updateInvGui();
			return true;
		} else {
			slot.setItem(null);
			slot.setItemCount(0);
			amountToRemove = amountToRemove - onSlot;
			updateInvGui();
			return removeItemFromInventory(item, amountToRemove);
		}
	}

	@Override
	public void afterRestart() {
		createSlots();
	}

	@Override
	public void handleInputs(Input input, float delta) {
		if (input.isKeyJustPressed(KeyBinds.INVENTORY)) {
			if (visible) {
				Globals.ngui.hide();
				input.setInputReceiver(InputStates.GAME);
				visible = false;
			} else {
				Globals.ngui.show("inv");
				input.setInputReceiver(InputStates.GUI);
				updateInvGui();
				visible = true;
			}
		}
	}

}
