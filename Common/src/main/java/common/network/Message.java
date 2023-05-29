package common.network;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 123456789;

	private String message, senderID, command;

	public Message(String message, String senderID, String command) {
		this.message = message;
		this.senderID = senderID;
		this.command = command;
	}

	public String getMessage() {
		return message;
	}
	
	public String getSenderID() {
		return senderID;
	}
	
	public String getCommand() {
		return command;
	}

}
