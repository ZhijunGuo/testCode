package com.example.galaxy08.message.iq;

import org.jivesoftware.smack.packet.IQ;

public class RedirectIQ extends IQ {
	
	public static final String ELEMENT="query";
	public static final String NAMESPACE = "urn:xmpp:cxfr";
	
	private String server;
	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@Override
	public String getChildElementXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<").append(ELEMENT).append(" xmlns:\"").append(NAMESPACE).append("\">");
		sb.append("<server>").append(server).append("</server>");
		sb.append("</").append(ELEMENT).append(">");
        return sb.toString();
	}

}
