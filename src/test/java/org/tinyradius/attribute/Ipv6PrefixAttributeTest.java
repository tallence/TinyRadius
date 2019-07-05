package org.tinyradius.attribute;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import org.apache.commons.codec.binary.Hex;
import org.tinyradius.util.RadiusException;

import static org.junit.Assert.assertEquals;

public class Ipv6PrefixAttributeTest {

	@Test
	public void test() throws DecoderException, RadiusException {
	     Ipv6PrefixAttribute attr = new Ipv6PrefixAttribute(97, "2003:4:12a0:500::/56");
	    assertEquals("2003:4:12a0:500:0:0:0:0/56", attr.getAttributeValue());
	    byte[] data = attr.getAttributeData();

            Hex hex = new Hex();
	    data = hex.decodeHex("610c0038200300041285094a");

	    Ipv6PrefixAttribute attr2 = new Ipv6PrefixAttribute();
	    attr2.readAttribute(data,0,data.length);
	    assertEquals("2003:4:1285:94a:0:0:0:0/56", attr2.getAttributeValue());
	}

}

