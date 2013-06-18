package com.example.galaxy08.message.iq;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.example.galaxy08.tool.DebugLog;

public class PingIQProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser xp) throws Exception {
		DebugLog.logd("MessageMaintenanceThread", "parseIQ");
		PingIQ iq = new PingIQ();
		iq.setPacketID(xp.getAttributeValue("", "id"));
		iq.setFrom(xp.getAttributeValue("", "from"));
		iq.setType(Type.RESULT);
		return iq;
	}

}
