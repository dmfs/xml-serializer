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
 * An XML Serializer class.
 * 
 * TODO: support other XML versions and encodings.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class XmlSerializer
{
	/**
	 * XML version 1.0 intro.
	 */
	private final static String XML_PREFIX = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";

	/**
	 * The XML root node.
	 */
	private XmlTag mRootNode;

	/**
	 * The {@link Writer} we write to.
	 */
	private Writer mOut;

	/**
	 * The {XmlNamespaceRegistry} for this document.
	 */
	private final XmlNamespaceRegistry mNamespaceRegistry = new XmlNamespaceRegistry();


	/**
	 * Construct a new serializer that writes to {@code out}.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 */
	public XmlSerializer(Writer out)
	{
		mOut = out;
	}


	/**
	 * Start the serializer.
	 * 
	 * @param rootNode
	 *            The XML root node of the document.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	public void serialize(XmlTag rootNode) throws IOException, InvalidStateException, InvalidValueException
	{
		mOut.write(XML_PREFIX);
		rootNode.setNamespaceRegistry(mNamespaceRegistry);
		rootNode.open(mOut);
		mRootNode = rootNode;
	}


	/**
	 * Start the serializer with an {@link IXmlTagSerializable} as root node.
	 * 
	 * @param root
	 *            The root node.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	public void serialize(IXmlTagSerializable root) throws IOException, InvalidStateException, InvalidValueException
	{
		serialize(new XmlTagSerializableAdapter(root));
	}


	/**
	 * Finish serialization, closing all open tags.
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public void close() throws InvalidStateException, IOException, InvalidValueException
	{
		mRootNode.close();
		mNamespaceRegistry.clear();
	}


	/**
	 * Register a namespace.
	 * <p>
	 * Use this method if you know that you'll add elements with certain namespaces at a later time and you want to ensure the namespaces are defined as early
	 * as possible.
	 * </p>
	 * 
	 * @param namespace
	 *            The namespace to register.
	 * @throws InvalidValueException
	 */
	public void registerNamespace(String namespace) throws InvalidValueException
	{
		mNamespaceRegistry.getNamespace(namespace);
	}
}
