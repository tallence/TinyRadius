/**
 * Created on 24/Jun/2016
 * @author Ivan F. Martinez
 */
package org.tinyradius.attribute;

import java.util.Arrays;
import java.net.Inet6Address;
import java.net.UnknownHostException;

import org.tinyradius.util.RadiusException;

/**
 * This class represents a Radius attribute for an IPv6 prefix.
 */
public class Ipv6PrefixAttribute extends RadiusAttribute {

	/**
	 * Constructs an empty IPv6 prefix attribute.
	 */
	public Ipv6PrefixAttribute() {
		super();
	}

	/**
	 * Constructs an IPv6 prefix attribute.
	 * @param type attribute type code
	 * @param value value, format: "ipv6 address"/prefix
	 */
	public Ipv6PrefixAttribute(int type, String value) {
		setAttributeType(type);
		setAttributeValue(value);
	}

	/**
	 * Returns the attribute value (IP number) as a string of the
	 * format "xx.xx.xx.xx".
	 * @see org.tinyradius.attribute.RadiusAttribute#getAttributeValue()
	 */
	public String getAttributeValue() {
		final byte[] data = getAttributeData();
		if (data == null || data.length <2|| data.length > 18)
			throw new RuntimeException("IPv6 prefix attribute: expected 4-20 bytes attribute data");
		try {
		        final int prefix = (data[1] & 0xff);
			final Inet6Address addr = (Inet6Address)Inet6Address.getByAddress(null, Arrays.copyOfRange(data,2,18));

			return addr.getHostAddress() + "/" + prefix;
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("bad IPv6 prefix", e);
		}

	}

	/**
	 * Sets the attribute value (IPv6 number/prefix). String format:
	 * ipv6 address.
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 * @see org.tinyradius.attribute.RadiusAttribute#setAttributeValue(java.lang.String)
	 */
	public void setAttributeValue(String value) {
		if (value == null || value.length() < 3)
			throw new IllegalArgumentException("bad IPv6 address : " + value);
		try {
		        final byte[] data = new byte[18];
		        data[0] = 0;
//TODO better checking
		        final int slashPos = value.indexOf("/");
		        data[1] = (byte)(Integer.valueOf(value.substring(slashPos+1)) & 0xff);

			final Inet6Address addr = (Inet6Address)Inet6Address.getByName(value.substring(0,slashPos));

			byte[] ipData = addr.getAddress();
			for (int i = 0; i < ipData.length; i++) {
			    data[i+2] = ipData[i];
			}

			setAttributeData(data);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException("bad IPv6 address : " + value, e);
		}
	}


	/**
	 * Check attribute length.
	 * @see org.tinyradius.attribute.RadiusAttribute#readAttribute(byte[], int, int)
	 */
	public void readAttribute(byte[] data, int offset, int length)
	throws RadiusException {
		// At least 4 (type, length, reserved, prefix-length for 0-byte prefix), up to 20 (for 16-byte prefix)
		if (length < 4 || length > 20) {
			String asString = Arrays.toString(Arrays.copyOfRange(data, offset, offset+length));
			throw new RadiusException("IPv6 prefix attribute: expected 4-20 bytes data, got " + length + " bytes: " + asString);
		}
		super.readAttribute(data, offset, length);
	}

}
