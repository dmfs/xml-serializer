package org.dmfs.xmlserializer;

import static org.dmfs.xmlserializer.XmlTestUtils.assertXmlEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlAttributeSerializableAdapterTest
{
	private final static String XML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";

	private XmlSerializer s;
	private StringWriter sw;


	@Before
	public void setUp() throws Exception
	{
		sw = new StringWriter();
		s = new XmlSerializer(sw);
	}


	/**
	 * Test insertion of an instance implementing IXmlElementSerializable.
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void testIXmlElementSerializable() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		s.serialize(new XmlElement("ns", "root").add(new IXmlAttributeSerializable()
		{
			public String getXmlAttributeNamespace()
			{
				return null;
			}


			public String getXmlAttributeName()
			{
				return "attr1";
			}


			public String getXmlAttributeValue()
			{
				return "val1";
			}

		}).add(new IXmlAttributeSerializable()
		{
			public String getXmlAttributeNamespace()
			{
				return "ns2";
			}


			public String getXmlAttributeName()
			{
				return "attr2";
			}


			public String getXmlAttributeValue()
			{
				return "val2";
			}

		}));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\" xmlns:B=\"ns2\" attr1=\"val1\" B:attr2=\"val2\"></A:root>", sw.toString());
	}
}
