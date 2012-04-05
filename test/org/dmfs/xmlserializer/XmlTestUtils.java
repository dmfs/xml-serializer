package org.dmfs.xmlserializer;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class XmlTestUtils
{

	/**
	 * Assert two XML documents are equal.
	 * 
	 * <p>
	 * Taken from: <a href="http://stackoverflow.com/questions/141993/best-way-to-compare-2-xml-documents-in-java">Stackoverflow: Best way to compare 2 XML
	 * documents in Java</a>
	 * </p>
	 * 
	 * @param expectedXml
	 *            A {@link String} containing the expected XML document.
	 * @param actualXml
	 *            A {@link String} containing the actual XML document.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	static void assertXmlEquals(String expectedXml, String actualXml) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setCoalescing(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setIgnoringComments(false);
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document doc1 = db.parse(new ByteArrayInputStream(expectedXml.getBytes()));
		doc1.normalizeDocument();

		Document doc2 = db.parse(new ByteArrayInputStream(actualXml.getBytes()));
		doc2.normalizeDocument();

		assertTrue(doc1.isEqualNode(doc2));

	}

}
