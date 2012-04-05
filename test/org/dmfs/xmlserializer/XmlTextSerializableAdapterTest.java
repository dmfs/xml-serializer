package org.dmfs.xmlserializer;

import static org.dmfs.xmlserializer.XmlTestUtils.assertXmlEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlTextSerializableAdapterTest
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
		s.serialize(new XmlElement("ns", "root").add(new IXmlTextSerializable()
		{

			public void populateXmlText(XmlText adapter) throws IOException, InvalidStateException
			{
				adapter.add("some text &<>");
				adapter.add("some more text");
			}

		}));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\">some text &amp;&lt;&gt;some more text</A:root>", sw.toString());
	}
}
