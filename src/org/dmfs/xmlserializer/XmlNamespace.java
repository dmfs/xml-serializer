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
 * Holds an XML namespace and its assigned prefix, if any.
 * 
 * Instances of this class are immutable.
 * 
 * For internal use only.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
final class XmlNamespace
{
	/**
	 * Static byte array containing the xmlns namespace prefix.
	 */
	private final static String XMLNS = "xmlns";

	/**
	 * The prefix for this namespace. May be {@code null}.
	 */
	public final String prefix;

	/**
	 * The namespace. Will never be {@code null}.
	 */
	public final String namespace;

	/**
	 * Indicates whether this namespace has a prefix assigned.
	 */
	public final boolean hasPrefix;


	/**
	 * Constructor for an XmlNamespace. For internal use only.
	 * 
	 * @param prefix
	 *            The prefix of this name space or null if this is a default name space.
	 * @param namespace
	 *            The name space.
	 * @throws InvalidValueException
	 *             if the name space is invalid.
	 */
	XmlNamespace(String prefix, String namespace) throws InvalidValueException
	{
		if (namespace == null)
		{
			throw new NullPointerException("Name spaces must not be null!");
		}
		if (namespace.length() == 0)
		{
			throw new InvalidValueException("Name spaces must not be empty!");
		}
		this.hasPrefix = prefix != null && prefix.length() > 0;
		this.prefix = hasPrefix ? prefix : null;
		this.namespace = namespace;
	}


	/**
	 * Write the assigned namespace prefix to the {@link Writer} {@code out}. If no prefix has been assigned nothing gets written.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @throws IOException
	 */
	void writePrefix(Writer out) throws IOException
	{
		if (hasPrefix)
		{
			out.write(prefix);
		}
	}


	/**
	 * Write the namespace definition to the {@link Writer} {@code out}.
	 * 
	 * If no prefix has been assigned this method writes a default namespace definition.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @throws IOException
	 */
	void write(Writer out) throws IOException
	{
		out.write(XMLNS);
		if (hasPrefix)
		{
			out.write(':');
			out.write(prefix);
		}
		out.write("=\"");
		XmlUtils.writeXmlEntityEncodedString(out, namespace);
		out.write('"');

	}


	@Override
	public int hashCode()
	{
		// if this namespace has a prefix take it into account!
		return hasPrefix ? prefix.hashCode() * 31 + namespace.hashCode() : namespace.hashCode();
	}


	@Override
	public boolean equals(Object object)
	{
		if (object == this || object == null || !(object instanceof XmlNamespace))
		{
			return object == this;

		}

		XmlNamespace ns = (XmlNamespace) object;
		// XmlNamespaces are equal if both, namespace and prefix (if any) are equal.
		return (hasPrefix && prefix.equals(ns.prefix) || !hasPrefix && !ns.hasPrefix) && (namespace.equals(ns.namespace));
	}
}
