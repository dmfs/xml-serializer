package org.dmfs.xmlserializer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;


public class XmlNamespaceTest
{

	/**
	 * Test the Constructor.
	 * @throws InvalidValueException 
	 */
	@Test
	public void testConstructor() throws InvalidValueException
	{
		XmlNamespace ns1 = new XmlNamespace("1234", "abc");
		assertEquals("1234", ns1.prefix);
		assertEquals("abc", ns1.namespace);
		assertTrue(ns1.hasPrefix);

		XmlNamespace ns2 = new XmlNamespace(null, "xyz");
		assertNull(ns2.prefix);
		assertEquals("xyz", ns2.namespace);
		assertFalse(ns2.hasPrefix);

		XmlNamespace ns3 = new XmlNamespace("", "xyz");
		assertNull(ns3.prefix);
		assertEquals("xyz", ns3.namespace);
		assertFalse(ns3.hasPrefix);
	}


	/**
	 * Check that the Constructor throws a NPE if namespace is {@code null}.
	 * @throws InvalidValueException 
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNPE1() throws InvalidValueException
	{
		new XmlNamespace("AAA", null);
	}


	/**
	 * Check that the Constructor throws an InvalidNamespaceException if namespace is an empty String.
	 * @throws InvalidValueException 
	 */
	@Test(expected = InvalidValueException.class)
	public void testConstructorNPE2() throws InvalidValueException
	{
		new XmlNamespace("AAA", "");
	}


	/**
	 * Check that the Constructor throws a NPE if namespace is {@code null}.
	 * @throws InvalidValueException 
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorNPE3() throws InvalidValueException
	{
		new XmlNamespace(null, null);
	}


	/**
	 * Test writePrefix().
	 * 
	 * @throws IOException
	 * @throws InvalidValueException 
	 */
	@Test
	public void testWritePrefix() throws IOException, InvalidValueException
	{
		StringWriter sw0 = new StringWriter();
		XmlNamespace ns0 = new XmlNamespace("prefix123", "some:name:space");
		ns0.writePrefix(sw0);
		// ensure the prefix has been written
		assertEquals("prefix123", sw0.toString());

		StringWriter sw1 = new StringWriter();
		XmlNamespace ns1 = new XmlNamespace(null, "some:name:space");
		ns1.writePrefix(sw1);
		// ensure nothing has been written
		assertEquals("", sw1.toString());

		StringWriter sw2 = new StringWriter();
		XmlNamespace ns2 = new XmlNamespace("", "some:name:space");
		ns2.writePrefix(sw2);
		// ensure nothing has been written
		assertEquals("", sw2.toString());
	}


	/**
	 * Test writing a namespace definition to an {@link OutputStream}.
	 * @throws IOException 
	 * @throws InvalidValueException 
	 */
	@Test
	public void testWrite() throws IOException, InvalidValueException
	{
		StringWriter sw0 = new StringWriter();
		XmlNamespace ns0 = new XmlNamespace("prefix123", "some:name:space");
		ns0.write(sw0);
		// ensure the prefix has been written
		assertEquals("xmlns:prefix123=\"some:name:space\"", sw0.toString());

		StringWriter sw1 = new StringWriter();
		XmlNamespace ns1 = new XmlNamespace(null, "some:name:space");
		ns1.write(sw1);
		// ensure nothing has been written
		assertEquals("xmlns=\"some:name:space\"", sw1.toString());

		StringWriter sw2 = new StringWriter();
		XmlNamespace ns2 = new XmlNamespace("", "some:name:space");
		ns2.write(sw2);
		// ensure nothing has been written
		assertEquals("xmlns=\"some:name:space\"", sw2.toString());

		StringWriter sw3 = new StringWriter();
		XmlNamespace ns3 = new XmlNamespace("XprefiX", "some&name<>space");
		ns3.write(sw3);
		// ensure nothing has been written
		assertEquals("xmlns:XprefiX=\"some&amp;name&lt;&gt;space\"", sw3.toString());
}


	/**
	 * Test hashCode():
	 * @throws InvalidValueException 
	 */
	@Test
	public void testHashCode() throws InvalidValueException
	{
		XmlNamespace ns0 = new XmlNamespace("X", "A");
		XmlNamespace ns1 = new XmlNamespace("X", "A");

		assertEquals(ns0, ns1);

		XmlNamespace ns2 = new XmlNamespace("XD", "jksdhlkfjashd");
		XmlNamespace ns3 = new XmlNamespace("XD", "jksdhlkfjashd");

		assertEquals(ns2, ns3);

		XmlNamespace ns4 = new XmlNamespace(null, "iouafabkjdvbaf");
		XmlNamespace ns5 = new XmlNamespace(null, "iouafabkjdvbaf");

		assertEquals(ns4, ns5);

	}


	/**
	 * Test equals().
	 * @throws InvalidValueException 
	 */
	@Test
	public void testEquals() throws InvalidValueException
	{
		XmlNamespace ns0 = new XmlNamespace("X", "A");

		XmlNamespace ns1 = new XmlNamespace("X", "A");
		XmlNamespace ns2 = new XmlNamespace("Y", "A");
		XmlNamespace ns3 = new XmlNamespace("X", "B");
		XmlNamespace ns4 = new XmlNamespace("Y", "B");

		XmlNamespace ns10 = new XmlNamespace(null, "A");
		XmlNamespace ns11 = new XmlNamespace(null, "A");
		XmlNamespace ns12 = new XmlNamespace(null, "B");

		assertTrue(ns0.equals(ns0));
		assertTrue(ns10.equals(ns10));
		assertFalse(ns0.equals(null));
		assertFalse(ns0.equals(new Object()));

		assertTrue(ns0.equals(ns1));
		assertTrue(ns1.equals(ns0));

		assertFalse(ns0.equals(ns2));
		assertFalse(ns2.equals(ns0));

		assertFalse(ns0.equals(ns3));
		assertFalse(ns3.equals(ns0));

		assertFalse(ns0.equals(ns4));
		assertFalse(ns4.equals(ns0));

		assertFalse(ns0.equals(ns11));
		assertFalse(ns11.equals(ns0));

		assertFalse(ns0.equals(ns12));
		assertFalse(ns12.equals(ns0));

		assertTrue(ns10.equals(ns11));
		assertTrue(ns11.equals(ns10));

		assertFalse(ns10.equals(ns12));
		assertFalse(ns12.equals(ns10));
	}
}
