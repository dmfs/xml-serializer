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
 * Class that represents an attribute of an XML tag.
 * 
 * Instances of this class are immutable.
 * <p>
 * <b>Note:</b> Two XmlAttributes are considered to be equal if their namespaces and their names are equal. The value is not taken into account! We do this to
 * ensure a {@link Set} of XmlAttributes won't contain multiple attributes with the same name.
 * </p>
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class XmlAttribute
{
	/**
	 * The namespace of this attribute or {@code null} if the attribute has no namespace.
	 */
	public final String namespace;

	/**
	 * An {@link XmlNamespace} instance representing the namespace or {@code null} if this attribute has no namespace or {@link setNamespaceRegistry} has not
	 * been called yet.
	 */
	public XmlNamespace mNamespace;

	/**
	 * A flag indicating whether this attribute has a namespace.
	 */
	public final boolean mHasNamespace;

	/**
	 * A {@link String} containing the name of this attribute.
	 */
	public final String name;

	/**
	 * A {@link String} containing the value of this attribute.
	 */
	public final String value;


	/**
	 * Constructor for an attribute without namespace.
	 * 
	 * @param name
	 *            A @{link String} with the attribute's name.
	 * @param value
	 *            A @{link String} with the attribute's value.
	 */
	public XmlAttribute(String name, String value)
	{
		this(null, name, value);
	}


	/**
	 * Constructor for an attribute with namespace.
	 * 
	 * @param namespace
	 *            A @{link String} containing the attribute's namespace.
	 * @param name
	 *            A @{link String} containing the attribute's name.
	 * @param value
	 *            A @{link String} containing the attribute's value.
	 */
	public XmlAttribute(String namespace, String name, String value)
	{
		if (name == null)
		{
			throw new NullPointerException("An attribute name must not be null!");
		}
		this.namespace = namespace;
		this.mHasNamespace = namespace != null && namespace.length() > 0;
		this.name = name;
		this.value = value;
	}


	/**
	 * Set the {@link XmlNamespaceRegistry} and register namespace.
	 * 
	 * Attributes do not store the namespace registry, they just use it to get an {@link XmlNamespace} for their namespace.
	 * 
	 * @param namespaceRegistry
	 *            The {@link XmlNamespaceRegistry} of this XML.
	 * @throws InvalidValueException
	 */
	final void setNamespaceRegistry(XmlNamespaceRegistry namespaceRegistry) throws InvalidValueException
	{
		if (mHasNamespace)
		{
			mNamespace = namespaceRegistry.getNamespace(namespace);
		}
	}


	/**
	 * Write the attribute definition to a {@link Writer}.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @throws IOException
	 */
	final void write(Writer out) throws IOException
	{
		if (mHasNamespace && mNamespace.hasPrefix) // only write a namespace if it has a prefix, attributes do not support default namespaces
		{
			mNamespace.writePrefix(out);
			out.write(':');
		}
		out.write(name);
		out.write("=\"");
		XmlUtils.writeXmlEntityEncodedString(out, value);
		out.write('"');
	}


	@Override
	final public int hashCode()
	{
		// if we have a namespace we take it into account.
		return mHasNamespace ? namespace.hashCode() * 31 + name.hashCode() : name.hashCode();
	}


	@Override
	final public boolean equals(Object object)
	{
		if (object == this || object == null || !(object instanceof XmlAttribute))
		{
			return object == this;

		}

		XmlAttribute o = (XmlAttribute) object;
		// attributes equal when their namespace and name are equal
		return (!mHasNamespace && !o.mHasNamespace || mHasNamespace && namespace.equals(o.namespace)) && name.equals(o.name);
	}
}
