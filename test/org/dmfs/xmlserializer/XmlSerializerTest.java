package org.dmfs.xmlserializer;

import static org.dmfs.xmlserializer.XmlTestUtils.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlSerializerTest
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


	@Test
	public void testRootNodeOnly1() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlElement("ns", "element"));
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testRootNodeOnly2() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlElement("element"));
		s.close();
		assertEquals(XML + "<element/>", sw.toString());
	}


	@Test
	public void testChildNodes1() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlElement("ns", "element2"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\"><A:element2/></A:element>", sw.toString());
	}


	@Test
	public void testChildNodes2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		root.add(new XmlElement("ns", "element2"));
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\"><A:element2/></A:element>", sw.toString());
	}


	@Test
	public void testChildNodes3() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlElement("ns2", "element2"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:element2/></A:element>", sw.toString());
	}


	@Test
	public void testChildNodes4() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		root.add(new XmlElement("ns2", "element2"));
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:element2/></A:element>", sw.toString());
		assertXmlEquals(XML + "<A:element xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:element2/></A:element>", sw.toString());
	}


	@Test
	public void testChildNodes5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlElement("ns2", "element2").add(new XmlElement("ns3", "element3")));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.endsWith("><B:element2><C:element3/></B:element2></A:element>"));
	}


	@Test
	public void testChildNodes6() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2 xmlns:C=\"ns3\"><C:element3/></B:element2></A:element>"));
	}


	@Test
	public void testChildNodes7() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		element2.add(new XmlElement("ns2", "element4"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2 xmlns:C=\"ns3\"><C:element3/><B:element4/></B:element2></A:element>"));
	}


	@Test
	public void testChildNodes8() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		element2.add(new XmlElement("ns2", "element4"));
		root.add(new XmlElement("ns", "element5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2 xmlns:C=\"ns3\"><C:element3/><B:element4/></B:element2><A:element5/></A:element>"));
	}


	@Test
	public void testChildNodes9() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		element2.add(new XmlElement("ns2", "element4"));
		root.add(new XmlElement("ns4", "element5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2 xmlns:C=\"ns3\"><C:element3/><B:element4/></B:element2><D:element5 xmlns:D=\"ns4\"/></A:element>"));
	}


	@Test
	public void testChildNodes10() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement(null, "element3"));
		element2.add(new XmlElement("ns2", "element4"));
		root.add(new XmlElement("ns4", "element5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2><element3/><B:element4/></B:element2><C:element5 xmlns:C=\"ns4\"/></A:element>"));
	}


	@Test
	public void testChildNodes10B() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("element3"));
		element2.add(new XmlElement("ns2", "element4"));
		root.add(new XmlElement("ns4", "element5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2><element3/><B:element4/></B:element2><C:element5 xmlns:C=\"ns4\"/></A:element>"));
	}


	@Test
	public void testChildNodes11() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		element2.add(new XmlElement("ns2", "element4"));
		root.add(new XmlElement("ns4", "element5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns2\""));
		assertTrue(xml.endsWith("><A:element2 xmlns:B=\"ns3\"><B:element3/><A:element4/></A:element2><C:element5 xmlns:C=\"ns4\"/></element>"));
	}


	@Test
	public void testChildNodes12() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlElement("ns", "element").add(new XmlElement("ns2", "element2").add(new XmlElement("ns3", "element3")).add(new XmlElement("ns2", "element4"))).add(
			new XmlElement("ns4", "element5")));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.contains(" xmlns:D=\"ns4\""));
		assertTrue(xml.endsWith("><B:element2><C:element3/><B:element4/></B:element2><D:element5/></A:element>"));
	}


	@Test
	public void testTextNodes1() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlText("plaintext"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\">plaintext</A:element>", sw.toString());
	}


	@Test
	public void testTextNodes2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlText("&&pl<ain>tex\"t'"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:element xmlns:A=\"ns\">&amp;&amp;pl&lt;ain&gt;tex&quot;t&apos;</A:element>", sw.toString());
	}


	@Test
	public void testTextNodes3() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlElement("ns2", "element2").add(new XmlElement("ns3", "element3").add(new XmlText("Hello"))));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertXmlEquals(XML + "<A:element xmlns:A=\"ns\" xmlns:B=\"ns2\" xmlns:C=\"ns3\"><B:element2><C:element3>Hello</C:element3></B:element2></A:element>", xml);
	}


	@Test
	public void testTextNodes4() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlElement("ns2", "element2").add(new XmlText("Hello")).add(new XmlElement("ns3", "element3")));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.endsWith("><B:element2>Hello<C:element3/></B:element2></A:element>"));
	}


	@Test
	public void testTextNodes5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		element2.add(new XmlText("HALLO"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2 xmlns:C=\"ns3\"><C:element3/>HALLO</B:element2></A:element>"));
	}


	@Test
	public void testRegisterNamespace() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		s.registerNamespace("ns3");
		XmlElement element2 = new XmlElement("ns2", "element2");
		root.add(element2);
		element2.add(new XmlElement("ns3", "element3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:C=\"ns2\""));
		assertTrue(xml.contains(" xmlns:B=\"ns3\""));
		assertTrue(xml.endsWith("><C:element2><B:element3/></C:element2></A:element>"));
	}


	@Test
	public void testAttribute1() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlElement("ns", "element").add(new XmlAttribute("name", "value")));
		s.close();
		assertEquals(XML + "<A:element name=\"value\" xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testAttribute2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		s.serialize(root);
		root.add(new XmlAttribute("name2", "value2"));
		s.close();
		assertEquals(XML + "<A:element name2=\"value2\" xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testAttribute3() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"/>"));
	}


	@Test
	public void testAttribute4() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlAttribute("name4", "value4"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.contains(" name4=\"value4\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"/>"));
	}


	@Test
	public void testAttribute5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlElement("ns", "element2"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"><A:element2/></A:element>"));
	}


	@Test
	public void testAttribute6() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlElement root = new XmlElement("ns", "element");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlElement("ns2", "element2"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:element name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:element2/></A:element>"));
	}


	@Test
	public void testComplexExample() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlElement root = new XmlElement("XmlLibrary", "Library");
		s.serialize(root);
		root.add(new XmlElement("XmlLibrary", "Shelf").addAttribute("location", "loc1")
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title1")).add(new XmlAttribute("author", "Author1")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title2")).add(new XmlAttribute("author", "Author2")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title3")).add(new XmlAttribute("author", "Author3"))));
		root.add(new XmlElement("XmlLibrary", "Shelf").addAttribute("location", "loc2")
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title4")).add(new XmlAttribute("author", "Author4")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title5")).add(new XmlAttribute("author", "Author5")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title6")).add(new XmlAttribute("author", "Author6"))));
		s.close();
		String xml = sw.toString();
		assertXmlEquals(XML + "<A:Library xmlns:A=\"XmlLibrary\" xmlns:B=\"XmlBook\" >" + "<A:Shelf location=\"loc1\">"
			+ "<B:Book title=\"Title1\" author=\"Author1\"/>" + "<B:Book title=\"Title2\" author=\"Author2\"/>"
			+ "<B:Book title=\"Title3\" author=\"Author3\"/>" + "</A:Shelf>" + "<A:Shelf location=\"loc2\">" + "<B:Book title=\"Title4\" author=\"Author4\"/>"
			+ "<B:Book title=\"Title5\" author=\"Author5\"/>" + "<B:Book title=\"Title6\" author=\"Author6\"/>" + "</A:Shelf>" + "</A:Library>", xml);
	}


	@Test
	public void testComplexExample2() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlElement root = new XmlElement("XmlLibrary", "Library");
		root.add(new XmlElement("XmlLibrary", "Shelf").addAttribute("location", "loc1")
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title1")).add(new XmlAttribute("author", "Author1")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title2")).add(new XmlAttribute("author", "Author2")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title3")).add(new XmlAttribute("author", "Author3"))));
		root.add(new XmlElement("XmlLibrary", "Shelf").addAttribute("location", "loc2")
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title4")).add(new XmlAttribute("author", "Author4")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title5")).add(new XmlAttribute("author", "Author5")))
			.add(new XmlElement("XmlBook", "Book").add(new XmlAttribute("title", "Title6")).add(new XmlAttribute("author", "Author6"))));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertXmlEquals(XML + "<A:Library xmlns:A=\"XmlLibrary\" xmlns:B=\"XmlBook\" >" + "<A:Shelf location=\"loc1\">"
			+ "<B:Book title=\"Title1\" author=\"Author1\"/>" + "<B:Book title=\"Title2\" author=\"Author2\"/>"
			+ "<B:Book title=\"Title3\" author=\"Author3\"/>" + "</A:Shelf>" + "<A:Shelf location=\"loc2\">" + "<B:Book title=\"Title4\" author=\"Author4\"/>"
			+ "<B:Book title=\"Title5\" author=\"Author5\"/>" + "<B:Book title=\"Title6\" author=\"Author6\"/>" + "</A:Shelf>" + "</A:Library>", xml);
	}
}
