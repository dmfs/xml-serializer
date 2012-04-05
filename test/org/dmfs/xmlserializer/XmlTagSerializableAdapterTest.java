package org.dmfs.xmlserializer;

import static org.dmfs.xmlserializer.XmlTestUtils.assertXmlEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlTagSerializableAdapterTest
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
	 * Test insertion of an instance implementing IXmlTagSerializable.
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void testIXmlTagSerializable() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		s.serialize(new XmlTag("ns", "root").add(new IXmlTagSerializable()
		{

			public String getXmlNamespace()
			{
				return "XNamespaceX";
			}


			public String getXmlTagName()
			{
				return "TAGNAME";
			}


			public void populateXmlTag(XmlTag adapter) throws IOException, InvalidStateException, InvalidValueException
			{
				// do nothing
			}

		}));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\" xmlns:B=\"XNamespaceX\"><B:TAGNAME /></A:root>", sw.toString());
	}

	private class TestIXmlSerializableAttributeClass extends Object implements IXmlTagSerializable
	{
		public String getXmlNamespace()
		{
			return "XNamespaceX";
		}


		public String getXmlTagName()
		{
			return "TAGNAME";
		}


		public void populateXmlTag(XmlTag adapter) throws IOException, InvalidStateException, InvalidValueException
		{
			// add some attributes
			adapter.addAttribute("testnamex", "testvalue1");
			adapter.add(new XmlAttribute("testns2", "testnamey", "testvalue2"));
		}
	}


	/**
	 * Test insertion of an instance implementing IXmlTagSerializable and IXmlAttributedSerializable.
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void testIXmlTagSerializableAttributes() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException,
		SAXException
	{
		s.serialize(new XmlTag("ns", "root").add(new TestIXmlSerializableAttributeClass()));
		s.close();
		assertXmlEquals(XML
			+ "<A:root xmlns:A=\"ns\" xmlns:B=\"XNamespaceX\" xmlns:C=\"testns2\"><B:TAGNAME testnamex=\"testvalue1\" C:testnamey=\"testvalue2\"/></A:root>",
			sw.toString());
	}

	private class TestIXmlSerializableChildrenClass extends Object implements IXmlTagSerializable
	{
		public String getXmlNamespace()
		{
			return "XNamespaceX";
		}


		public String getXmlTagName()
		{
			return "TAGNAME";
		}


		public void populateXmlTag(XmlTag adapter) throws IOException, InvalidStateException, InvalidValueException
		{
			// add some child nodes
			adapter.add(new XmlTag("testns3", "testtag"));
			adapter.addText("TextVALUE");
		}
	}


	/**
	 * Test insertion of an instance implementing IXmlTagSerializable and IXmlChildrenSerializable.
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void testIXmlSerializableChildren() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		s.serialize(new XmlTag("ns", "root").add(new TestIXmlSerializableChildrenClass()));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\" xmlns:B=\"XNamespaceX\" xmlns:C=\"testns3\"><B:TAGNAME><C:testtag/>TextVALUE</B:TAGNAME></A:root>",
			sw.toString());
	}
}
