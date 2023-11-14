package client.gui.inventory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koossa.filesystem.Files;
import com.koossa.logger.Log;
import com.koossa.savelib.ISavable;
import com.koossa.savelib.SaveSystem;

public class InventoryRegistry {
	
	private static Map<String, InvItem> allItems = new HashMap<String, InvItem>();
	
	
	
	public static void loadAllItemsFromFolder(String folderPath) {
		Log.debug(InventoryRegistry.class, "Loading inventory items from: " + folderPath);
		
		File folder = new File(Files.getFolder("Data"), folderPath);
		Files.validateFolder(folder);
		File[] files = folder.listFiles();
		if (files.length <= 0) {
			InvItemCollection obj = new InvItemCollection();
			obj.items = new ArrayList<InvItem>();
			obj.items.add(new InvItem());
			obj.items.add(new InvItem());
			obj.items.add(new InvItem());
			obj.items.add(new InvItem());
			SaveSystem.save(obj, true, folderPath, "template");
		}
		
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			InvItemCollection coll = SaveSystem.load(InvItemCollection.class, true, folderPath, file.getName());
			if (coll == null) {
				Log.error(InventoryRegistry.class, "Failed to load file with inventory item data from: " + folderPath + ". File named: " + file.getName());
			} else {
				coll.items.forEach(item -> {
					if (item.getItemName().length() > 0 && item.getItemName() != null && !item.getItemName().equalsIgnoreCase("none")) {
						InvItem it = allItems.putIfAbsent(item.getItemName().toLowerCase(), item);
						if (it != null) {
							Log.error(InventoryRegistry.class, "Item already exists in registry: " + item.getItemName());
						}
					}
				});
			}
		}
		Log.debug(InventoryRegistry.class, "Loading inventory items from: " + folderPath + " completed.");
	}
	
	public static InvItem getItem(String name) {
		return allItems.getOrDefault(name, null);
	}

}

class InvItemCollection implements ISavable {
	protected List<InvItem> items;
}
