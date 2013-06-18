package com.example.galaxy08.message.iq;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class RedirectIQProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser xp) throws Exception {
		RedirectIQ iq = new RedirectIQ();
		while (true) {
			int n = xp.next();
			if (n == XmlPullParser.START_TAG) {
				if ("server".equals(xp.getName())) {
					iq.setServer(xp.nextText());
				}
			} else if (n == XmlPullParser.END_TAG) {
				if ("query".equals(xp.getName())) {
					break;
				}
			}
		}
		return iq;
	}

}