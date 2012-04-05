package org.dmfs.xmlserializer;

import static org.dmfs.xmlserializer.XmlTestUtils.assertXmlEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class XmlCommentTest
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
	public void testConstructor() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		StringWriter sw = new StringWriter();
		XmlSerializer s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("hello\n")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--hello\n--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!----></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment(null)));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!----></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment()));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!----></A:root>", sw.toString());
	}


	@Test(expected = InvalidValueException.class)
	public void testConstructorException() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("he--llo\n")));
		s.close();
	}


	@Test
	public void testAdd() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		StringWriter sw = new StringWriter();
		XmlSerializer s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("hello\n").add("some comment")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--hello\nsome comment--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("").add("some comment")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--some comment--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment(null).add("some comment")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--some comment--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment().add("some comment")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--some comment--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		s.serialize(new XmlElement("ns", "root").add(new XmlComment().add("some comment\n").add("some other comment")));
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--some comment\nsome other comment--></A:root>", sw.toString());

		sw = new StringWriter();
		s = new XmlSerializer(sw);
		XmlElement root = new XmlElement("ns", "root");
		XmlComment comment = new XmlComment();
		s.serialize(root);
		root.add(comment);
		comment.add("some comment\n");
		comment.add("some other comment");
		s.close();
		assertXmlEquals(XML + "<A:root xmlns:A=\"ns\"><!--some comment\nsome other comment--></A:root>", sw.toString());

	}


	@Test(expected = InvalidValueException.class)
	public void testAddInvalid() throws InvalidStateException, IOException, InvalidValueException, ParserConfigurationException, SAXException
	{
		s.serialize(new XmlElement("ns", "root").add(new XmlComment("hello\n").add("some -- comment")));
		s.close();
	}
}
