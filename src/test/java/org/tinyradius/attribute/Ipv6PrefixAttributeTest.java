package org.tinyradius.attribute;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.tinyradius.util.RadiusException;

import static org.junit.Assert.assertEquals;

public class Ipv6PrefixAttributeTest {

	@Test
	public void test_fromString() {
		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute(97, "2003:4:12a0:500::/56");
		assertEquals("2003:4:12a0:500:0:0:0:0/56", attr.getAttributeValue());
	}

	@Test
	public void test_fromBytes() throws DecoderException, RadiusException {
		byte[] data = Hex.decodeHex("610c0038200300041285094a");

		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, data.length);
		assertEquals("2003:4:1285:94a:0:0:0:0/56", attr.getAttributeValue());
	}

	@Test(expected = RadiusException.class)
	public void test_tooFewBytes_throwsException() throws Exception {
		byte[] data = Hex.decodeHex("610c00");

		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, data.length);
	}

	@Test(expected = RadiusException.class)
	public void test_tooManyBytes_throwsException() throws Exception {
		byte[] data = Hex.decodeHex("610c0038200300041285094a38a020b4224789237a");

		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, data.length);
	}

}

