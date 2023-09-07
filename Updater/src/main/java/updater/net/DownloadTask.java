package updater.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class DownloadTask extends Thread {
	
	private String url;
	private byte[] data;
	private URLConnection myURLConnection;
	private File targetFolder, targetFile;
	private String name;
	
	public DownloadTask(String url, String targetFolderPath, String name) {
		this.url = url;
		this.targetFolder = new File(targetFolderPath);
		this.targetFile = new File(targetFolder, name);
		if (!targetFolder.exists()) {
			targetFolder.mkdirs();
		}
		start();
	}
	
	@Override
	public void run() {
		try {
			myURLConnection = new URL(url).openConnection();
		    myURLConnection.connect();
		    long targetSize = myURLConnection.getContentLengthLong();
		    long localSize = getLocalFileSize();
		    if (localSize != targetSize) {
		    	System.out.println("Downloading file: " + targetFile.getPath());
			    data = myURLConnection.getInputStream().readAllBytes();
			    FileOutputStream os = new FileOutputStream(targetFile);
			    os.write(data);
			    os.close();
		    }
		} 
		catch (MalformedURLException e) { 
		    e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private long getLocalFileSize() {
		try {
			return Files.size(targetFile.toPath());
		} catch (IOException e) {
			return 0;
		}
	}

}
