/*
 * dmfs - http://dmfs.org/
 *
 * Copyright (C) 2012 Marten Gajda <marten@dmfs.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package org.dmfs.xmlserializer;

import java.io.IOException;
import java.io.Writer;


/**
 * A collection of useful utilities for the XML serializer.
 * 
 * TODO: make use of the StringUtils package instead of repeating some of its methods here.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 * 
 */
final class XmlUtils
{

	/**
	 * The XML entities.
	 */
	private final static String XML_ENTITY_QUOT = "&quot;";
	private final static String XML_ENTITY_AMP = "&amp;";
	private final static String XML_ENTITY_APOS = "&apos;";
	private final static String XML_ENTITY_LT = "&lt;";
	private final static String XML_ENTITY_GT = "&gt;";

	/**
	 * Xml special characters that have to be encoded by an entity.
	 */
	private final static String XML_SPECIAL_CHARS = "\"&'<>";


	/**
	 * Private constructor. No instantiation allowed.
	 */
	private XmlUtils()
	{
	}


	/**
	 * Writes {@link String} {@code s} to the {@link Writer} {@code out} replacing special XML tokens by their respective XML entities.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @param s
	 *            The raw {@link String}.
	 * 
	 * @throws IOException
	 */
	public static void writeXmlEntityEncodedString(Writer out, String s) throws IOException
	{
		if (s == null || s.length() == 0)
		{
			return;
		}

		int pos;
		int start = 0;

		// scan for special characters
		while ((pos = minIndexOfOneOf(s, start, XML_SPECIAL_CHARS)) >= 0)
		{
			// write everything up to the special character
			out.write(s.substring(start, pos));

			// write the XML entity
			switch (s.charAt(pos))
			{
				case '"':
					out.write(XML_ENTITY_QUOT);
					break;
				case '&':
					out.write(XML_ENTITY_AMP);
					break;
				case '\'':
					out.write(XML_ENTITY_APOS);
					break;
				case '<':
					out.write(XML_ENTITY_LT);
					break;
				case '>':
					out.write(XML_ENTITY_GT);
					break;
			}
			// skip the special character
			start = pos + 1;
		}
		// write everything that's left
		out.write(s.substring(start));
	}


	/**
	 * Find the first occurrence of any character in needles in string from the position start on.
	 * 
	 * @param string
	 *            {@link String} to scan.
	 * 
	 * @param start
	 *            Position of first character to take into account.
	 * 
	 * @param needles
	 *            Characters to scan for.
	 * 
	 * @return The first occurrence of any element of needles or {@code -1} if none was found.
	 * 
	 */
	public static int minIndexOfOneOf(String string, int start, String needles)
	{
		if (string == null || string.length() == 0)
		{
			return -1;
		}

		int len = string.length();

		while (start < len)
		{
			if (needles.indexOf(string.charAt(start)) >= 0)
			{
				return start;
			}
			++start;
		}
		return -1;
	}

}
