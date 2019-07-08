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

	@Test
	public void test_zeroBytePrefix() throws Exception {
		byte[] data = Hex.decodeHex("61040000");
		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, 4);
		assertEquals("0:0:0:0:0:0:0:0/0", attr.getAttributeValue());
	}

	@Test
	public void test_16BytePrefix() throws Exception {
		byte[] data = Hex.decodeHex("61140040200300041285094a38a020b422478923");
		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, 20);
		assertEquals("2003:4:1285:94a:38a0:20b4:2247:8923/64", attr.getAttributeValue());

	}

	@Test(expected = RadiusException.class)
	public void test_tooFewBytes_throwsException() throws Exception {
		byte[] data = Hex.decodeHex("610300");

		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, data.length);
	}

	@Test(expected = RadiusException.class)
	public void test_tooManyBytes_throwsException() throws Exception {
		byte[] data = Hex.decodeHex("61150038200300041285094a38a020b4224789237a");

		Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute();
		attr.readAttribute(data, 0, data.length);
	}

}

