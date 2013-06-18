/**
 * 
 */
package com.example.galaxy08.message.iq;

import org.jivesoftware.smack.packet.IQ;

import com.example.galaxy08.message.MessageManager;

/**
 *
 */
public class PingIQ extends IQ {
	
	public static String ELEMENT = "ping";
	
	public static String NAME_SPACE = "urn:xmpp:ping";
	
	public PingIQ(){
//		this.setType(Type.GET);
		this.setTo(MessageManager.CHAT_SERVER_DOMAIN);
	}
	
	public PingIQ(String id){
		this();
		this.setPacketID(id);
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
	 */
	@Override
	public String getChildElementXML() {
//		return Tool.combineStrings("<ping xmlns=\"",NAME_SPACE,"\"/>");
		return "";
	}

	
}
