package updater;

import updater.net.DownloadTask;

public class Updater {
	
	public static void main(String[] args) {
		new DownloadTask("https://raw.githubusercontent.com/KoosSA/Game/master/Client/Resources/Shaders/staticVertex.glsl", "D:\\Users\\Kosie\\Documents\\Eclipse Workspace\\Game\\Updater/Test", "staticVertex.glsl");
		System.out.println("Stor");
	}

}
