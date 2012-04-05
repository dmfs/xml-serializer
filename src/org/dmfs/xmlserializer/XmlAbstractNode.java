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
 * An abstract XML node.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public abstract class XmlAbstractNode
{
	/**
	 * State for new nodes.
	 */
	final static int STATE_NEW = 0;

	/**
	 * State indicating the node has been opened and the start tag has been opened.
	 */
	final static int STATE_START_TAG_OPEN = 1;

	/**
	 * State indication the node has been opened and the start tag has been closed, but the end tag is not written yet.
	 */
	final static int STATE_START_TAG_CLOSED = 2;

	/**
	 * State indication the node has been closed (i.e. the end tag has been written).
	 */
	final static int STATE_CLOSED = 3;

	/**
	 * The state of a node, initialized with {@link STATE_NEW}.
	 */
	int state = STATE_NEW;

	/**
	 * The depth at which this node is located in the XML tree. Until this node has been added to a tree it is considered to be the root element.
	 */
	private int mDepth = 0;


	/**
	 * Set the depth of this node (i.e. at which level it is located in the XML node tree).
	 * 
	 * @param depth
	 *            The depth.
	 */
	void setDepth(int depth)
	{
		mDepth = depth;
	}


	/**
	 * Get the depth of this node (i.e. at which level it is located in the XML node tree).
	 * 
	 * @return The depth.
	 */
	final int getDepth()
	{
		return mDepth;
	}


	/**
	 * Assign the {@link XmlNamespaceRegistry} that belongs to this XML tree.
	 * 
	 * @param namespaceRegistry
	 *            The {@link XmlNamespaceRegistry} of this XMl tree.
	 * @throws InvalidValueException
	 */
	abstract void setNamespaceRegistry(XmlNamespaceRegistry namespaceRegistry) throws InvalidValueException;


	/**
	 * Open this node for writing to a {@link Writer}
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	abstract void open(Writer out) throws IOException, InvalidStateException, InvalidValueException;


	/**
	 * Close this node, flushing out all unwritten content.
	 * 
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	abstract void close() throws IOException, InvalidStateException, InvalidValueException;
}
