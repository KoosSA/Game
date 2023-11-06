package client.gui.inventory;

import java.util.ArrayList;
import java.util.List;

import com.koossa.filesystem.CommonFolders;
import com.koossa.filesystem.Files;
import com.koossa.logger.Log;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.events.NiftyMouseSecondaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class InventoryController implements ScreenController {

	private Element rootPanel;
	private List<Element> guiSlots;
	private Screen screen;
	private List<InvSlotData> slots;
	private int numPerRow = 5;

	@Override
	public void bind(Nifty nifty, Screen screen) {
		guiSlots = new ArrayList<Element>();
		this.screen = screen;
		rootPanel = screen.findElementById("slotpanel");
		createSlots();
	}

	private void createSlots() {
		int mainRows = 5;
		int restSlot = 0;
		int restRows = 0;
		if (slots != null) {
			mainRows = slots.size() / numPerRow;
			restSlot = slots.size() % numPerRow;
			if (restSlot > 0)
				restRows = 1;
		} else {
			return;
		}

		guiSlots.clear();
		
		new PanelBuilder() {
			{
				alignLeft();
				width("*");
				height("10px");
				width("50px");
				invisibleToMouse();
			}
		}.build(rootPanel);

		
		
		for (int row = 0; row < mainRows + restRows; row++) {

			Element r = createRow(row);

			new PanelBuilder() {
				{
					alignLeft();
					width("*");
					height("10px");
					width("50px");
					invisibleToMouse();
				}
			}.build(rootPanel);

			new PanelBuilder() {
				{
					alignLeft();
					width("10px");
					height("50px");
					invisibleToMouse();
				}
			}.build(r);

			for (int i = 0; i < ((row < mainRows) ? numPerRow : restSlot); i++) {
				
				int index = (numPerRow * row + i);
				
				Element slot = new ControlBuilder("inv_" + index, "invSlot") {
					{
						width("50px");
						height("50px");
					}
				}.build(r);

				guiSlots.add(slot);

				new PanelBuilder() {
					{
						alignLeft();
						width("10px");
						height("50px");
						invisibleToMouse();
					}
				}.build(r);
			}
		}
		screen.layoutLayers();
	}

	private Element createRow(int row) {
		Element r = new PanelBuilder("row_" + row) {
			{
				childLayoutHorizontal();
				alignLeft();
				invisibleToMouse();
			}
		}.build(rootPanel);
		return r;
	}

	@Override
	public void onEndScreen() {
		// guiSlots.clear();
	}

	@NiftyEventSubscriber(pattern = "inv_.*")
	public void onClick(final String slotId, final NiftyMousePrimaryClickedEvent event) {
		int index = Integer.parseInt(slotId.split("_")[1]);
		Log.debug(this, "Slot " + index + " was just left clicked.");
	}
	
	@NiftyEventSubscriber(pattern = "inv_.*")
	public void onClick(final String slotId, final NiftyMouseSecondaryClickedEvent event) {
		int index = Integer.parseInt(slotId.split("_")[1]);
		Log.debug(this, "Slot " + index + " was just right clicked.");
	}
	

	@Override
	public void onStartScreen() {

	}

	public void updateGui(List<InvSlotData> slots) {
		this.slots = slots;
		if (guiSlots == null || slots == null)
			return;
		if (guiSlots.size() != slots.size()) {
			Log.error(this, "The amount of inventory slot data does not match the visual amount of slots.");
			return;
		}
		for (int i = 0; i < slots.size(); i++) {
			InvSlotData data = slots.get(i);
			if (data.getItemCount() > 0 && data.getItem() != null) {
				guiSlots.get(i).getNiftyControl(InvSlotControl.class).setText("x" + data.getItemCount());
				guiSlots.get(i).getNiftyControl(InvSlotControl.class).setIcon(
						Files.getCommonFolderPath(CommonFolders.Gui) + "/Icons/" + data.getItem().getIcon());
			} else {
				guiSlots.get(i).getNiftyControl(InvSlotControl.class).setText("");
				guiSlots.get(i).getNiftyControl(InvSlotControl.class).setIcon(null);
			}
		}
	}

	public void createSlots(List<InvSlotData> slots2) {
		this.slots = slots2;
		createSlots();
		updateGui(slots2);
	}
}
