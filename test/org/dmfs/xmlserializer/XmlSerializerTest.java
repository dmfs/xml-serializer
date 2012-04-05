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
		s.serialize(new XmlTag("ns", "tag"));
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testRootNodeOnly2() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlTag("tag"));
		s.close();
		assertEquals(XML + "<tag/>", sw.toString());
	}


	@Test
	public void testChildNodes1() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlTag("ns", "tag2"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\"><A:tag2/></A:tag>", sw.toString());
	}


	@Test
	public void testChildNodes2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		root.add(new XmlTag("ns", "tag2"));
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\"><A:tag2/></A:tag>", sw.toString());
	}


	@Test
	public void testChildNodes3() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlTag("ns2", "tag2"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:tag2/></A:tag>", sw.toString());
	}


	@Test
	public void testChildNodes4() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		root.add(new XmlTag("ns2", "tag2"));
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:tag2/></A:tag>", sw.toString());
		assertXmlEquals(XML + "<A:tag xmlns:A=\"ns\" xmlns:B=\"ns2\"><B:tag2/></A:tag>", sw.toString());
	}


	@Test
	public void testChildNodes5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlTag("ns2", "tag2").add(new XmlTag("ns3", "tag3")));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.endsWith("><B:tag2><C:tag3/></B:tag2></A:tag>"));
	}


	@Test
	public void testChildNodes6() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2 xmlns:C=\"ns3\"><C:tag3/></B:tag2></A:tag>"));
	}


	@Test
	public void testChildNodes7() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2 xmlns:C=\"ns3\"><C:tag3/><B:tag4/></B:tag2></A:tag>"));
	}


	@Test
	public void testChildNodes8() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		root.add(new XmlTag("ns", "tag5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2 xmlns:C=\"ns3\"><C:tag3/><B:tag4/></B:tag2><A:tag5/></A:tag>"));
	}


	@Test
	public void testChildNodes9() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		root.add(new XmlTag("ns4", "tag5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2 xmlns:C=\"ns3\"><C:tag3/><B:tag4/></B:tag2><D:tag5 xmlns:D=\"ns4\"/></A:tag>"));
	}


	@Test
	public void testChildNodes10() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag(null, "tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		root.add(new XmlTag("ns4", "tag5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2><tag3/><B:tag4/></B:tag2><C:tag5 xmlns:C=\"ns4\"/></A:tag>"));
	}


	@Test
	public void testChildNodes10B() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		root.add(new XmlTag("ns4", "tag5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2><tag3/><B:tag4/></B:tag2><C:tag5 xmlns:C=\"ns4\"/></A:tag>"));
	}


	@Test
	public void testChildNodes11() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		tag2.add(new XmlTag("ns2", "tag4"));
		root.add(new XmlTag("ns4", "tag5"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns2\""));
		assertTrue(xml.endsWith("><A:tag2 xmlns:B=\"ns3\"><B:tag3/><A:tag4/></A:tag2><C:tag5 xmlns:C=\"ns4\"/></tag>"));
	}


	@Test
	public void testChildNodes12() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlTag("ns", "tag").add(new XmlTag("ns2", "tag2").add(new XmlTag("ns3", "tag3")).add(new XmlTag("ns2", "tag4"))).add(
			new XmlTag("ns4", "tag5")));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.contains(" xmlns:D=\"ns4\""));
		assertTrue(xml.endsWith("><B:tag2><C:tag3/><B:tag4/></B:tag2><D:tag5/></A:tag>"));
	}


	@Test
	public void testTextNodes1() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlText("plaintext"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\">plaintext</A:tag>", sw.toString());
	}


	@Test
	public void testTextNodes2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlText("&&pl<ain>tex\"t'"));
		s.serialize(root);
		s.close();
		assertEquals(XML + "<A:tag xmlns:A=\"ns\">&amp;&amp;pl&lt;ain&gt;tex&quot;t&apos;</A:tag>", sw.toString());
	}


	@Test
	public void testTextNodes3() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlTag("ns2", "tag2").add(new XmlTag("ns3", "tag3").add(new XmlText("Hello"))));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertXmlEquals(XML + "<A:tag xmlns:A=\"ns\" xmlns:B=\"ns2\" xmlns:C=\"ns3\"><B:tag2><C:tag3>Hello</C:tag3></B:tag2></A:tag>", xml);
	}


	@Test
	public void testTextNodes4() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlTag("ns2", "tag2").add(new XmlText("Hello")).add(new XmlTag("ns3", "tag3")));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.contains(" xmlns:C=\"ns3\""));
		assertTrue(xml.endsWith("><B:tag2>Hello<C:tag3/></B:tag2></A:tag>"));
	}


	@Test
	public void testTextNodes5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		tag2.add(new XmlText("HALLO"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2 xmlns:C=\"ns3\"><C:tag3/>HALLO</B:tag2></A:tag>"));
	}


	@Test
	public void testRegisterNamespace() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		s.registerNamespace("ns3");
		XmlTag tag2 = new XmlTag("ns2", "tag2");
		root.add(tag2);
		tag2.add(new XmlTag("ns3", "tag3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag xmlns:"));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:C=\"ns2\""));
		assertTrue(xml.contains(" xmlns:B=\"ns3\""));
		assertTrue(xml.endsWith("><C:tag2><B:tag3/></C:tag2></A:tag>"));
	}


	@Test
	public void testAttribute1() throws IOException, InvalidStateException, InvalidValueException
	{
		s.serialize(new XmlTag("ns", "tag").add(new XmlAttribute("name", "value")));
		s.close();
		assertEquals(XML + "<A:tag name=\"value\" xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testAttribute2() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		s.serialize(root);
		root.add(new XmlAttribute("name2", "value2"));
		s.close();
		assertEquals(XML + "<A:tag name2=\"value2\" xmlns:A=\"ns\"/>", sw.toString());
	}


	@Test
	public void testAttribute3() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"/>"));
	}


	@Test
	public void testAttribute4() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlAttribute("name4", "value4"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.contains(" name4=\"value4\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"/>"));
	}


	@Test
	public void testAttribute5() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlTag("ns", "tag2"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.endsWith(" xmlns:A=\"ns\"><A:tag2/></A:tag>"));
	}


	@Test
	public void testAttribute6() throws IOException, InvalidStateException, InvalidValueException
	{
		XmlTag root = new XmlTag("ns", "tag");
		root.add(new XmlAttribute("name2", "value2"));
		s.serialize(root);
		root.add(new XmlAttribute("name3", "value3"));
		root.add(new XmlTag("ns2", "tag2"));
		s.close();
		String xml = sw.toString();
		assertTrue(xml.startsWith(XML + "<A:tag name"));
		assertTrue(xml.contains(" name2=\"value2\""));
		assertTrue(xml.contains(" name3=\"value3\""));
		assertTrue(xml.contains(" xmlns:A=\"ns\""));
		assertTrue(xml.contains(" xmlns:B=\"ns2\""));
		assertTrue(xml.endsWith("><B:tag2/></A:tag>"));
	}


	@Test
	public void testComplexExample() throws IOException, InvalidStateException, InvalidValueException, ParserConfigurationException, SAXException
	{
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		s.serialize(root);
		root.add(new XmlTag("XmlLibrary", "Shelf").addAttribute("location", "loc1")
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title1")).add(new XmlAttribute("author", "Author1")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title2")).add(new XmlAttribute("author", "Author2")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title3")).add(new XmlAttribute("author", "Author3"))));
		root.add(new XmlTag("XmlLibrary", "Shelf").addAttribute("location", "loc2")
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title4")).add(new XmlAttribute("author", "Author4")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title5")).add(new XmlAttribute("author", "Author5")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title6")).add(new XmlAttribute("author", "Author6"))));
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
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		root.add(new XmlTag("XmlLibrary", "Shelf").addAttribute("location", "loc1")
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title1")).add(new XmlAttribute("author", "Author1")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title2")).add(new XmlAttribute("author", "Author2")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title3")).add(new XmlAttribute("author", "Author3"))));
		root.add(new XmlTag("XmlLibrary", "Shelf").addAttribute("location", "loc2")
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title4")).add(new XmlAttribute("author", "Author4")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title5")).add(new XmlAttribute("author", "Author5")))
			.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title6")).add(new XmlAttribute("author", "Author6"))));
		s.serialize(root);
		s.close();
		String xml = sw.toString();
		assertXmlEquals(XML + "<A:Library xmlns:A=\"XmlLibrary\" xmlns:B=\"XmlBook\" >" + "<A:Shelf location=\"loc1\">"
			+ "<B:Book title=\"Title1\" author=\"Author1\"/>" + "<B:Book title=\"Title2\" author=\"Author2\"/>"
			+ "<B:Book title=\"Title3\" author=\"Author3\"/>" + "</A:Shelf>" + "<A:Shelf location=\"loc2\">" + "<B:Book title=\"Title4\" author=\"Author4\"/>"
			+ "<B:Book title=\"Title5\" author=\"Author5\"/>" + "<B:Book title=\"Title6\" author=\"Author6\"/>" + "</A:Shelf>" + "</A:Library>", xml);
	}
}
