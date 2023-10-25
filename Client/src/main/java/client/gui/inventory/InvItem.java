package client.gui.inventory;

public class InvItem {
	
	private String itemName = "None";
	private String useText = "Use";
	private int maxStackSize = 100;
	private boolean isStackable = true;
	private boolean isUsable = false;
	private String icon = "icon.png";
	
	public String getItemName() {
		return itemName;
	}
	
	public String getUseText() {
		return useText;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public boolean isStackable() {
		return isStackable;
	}
	
	public boolean isUsable() {
		return isUsable;
	}
	
	public String getIcon() {
		return icon;
	}
}
