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
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A class representing an XML tag.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class XmlTag extends XmlAbstractNode
{

	/**
	 * A {@link String identifying the namespace of this node or {@code null} if this node has no namespace .
	 */
	private final String mNamespaceString;

	/**
	 * An {@link XmlNamespace} or {@code null} if this node has no namespace or no {@link NamespaceRegistry} has net been set yet.
	 */
	private XmlNamespace mNamespace;

	/**
	 * The name of this tag.
	 */
	private final String mTagName;

	/**
	 * A {@link List} of {@link XmlAbstractNode}s that are children of this node or {@code null} if this node has no children (yet).
	 */
	private List<XmlAbstractNode> mChildren;

	/**
	 * A {@link Set} of {@link XmlAttribute}s for this node or {@code null} if no attributes have been added (yet).
	 */
	private Set<XmlAttribute> mAttributes;

	/**
	 * The childe node that is currently open or {@code null} if no child node is open.
	 */
	private XmlAbstractNode mOpenChild = null;

	/**
	 * A Flag that indicates whether this node has any child nodes.
	 */
	private boolean mHasChildren = false;

	/**
	 * The {@link Writer} the XML is written to.
	 */
	private Writer mOut;

	/**
	 * The {@link XmlNamespaceRegistry} of this XML document.
	 */
	XmlNamespaceRegistry mNamespaceRegistry;


	/**
	 * Constructor for a new tag with namespace.
	 * 
	 * @param namespace
	 *            A {@link String} containing the namespace of this tag.
	 * @param tag
	 *            A {@link String} containing the name of this tag.
	 */
	public XmlTag(String namespace, String tag)
	{
		mNamespaceString = namespace;
		if (tag == null)
		{
			throw new NullPointerException("Tag name must not be null");
		}
		mTagName = tag;
	}


	/**
	 * Constructor for a new tag without namespace.
	 * 
	 * @param tag
	 *            A {@link String} containing the name of this tag.
	 */
	public XmlTag(String tag)
	{
		this(null, tag);
	}


	@Override
	final void setDepth(int depth)
	{
		super.setDepth(depth);

		// update depth of all children!
		if (mChildren != null)
		{
			for (XmlAbstractNode node : mChildren)
			{
				node.setDepth(getDepth() + 1);
			}
		}

	}


	/**
	 * Add another {@link XmlAbstractNode} as child to this node.
	 * 
	 * @param node
	 *            The new child node.
	 * @return This XmlTag instance.
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public final XmlTag add(XmlAbstractNode node) throws InvalidStateException, IOException, InvalidValueException
	{
		switch (state)
		{
			case STATE_NEW:
				/*
				 * If the output is not open yet we cache all nodes to write them later when the node gets opened.
				 */
				if (mChildren == null)
				{
					mChildren = new ArrayList<XmlAbstractNode>();
					mHasChildren = true;
				}
				mChildren.add(node);
				if (mNamespaceRegistry != null)
				{
					node.setNamespaceRegistry(mNamespaceRegistry);
				}
				node.setDepth(getDepth() + 1);
				break;

			case STATE_START_TAG_OPEN:

				mOpenChild = node;

				// prepare child
				node.setNamespaceRegistry(mNamespaceRegistry);
				node.setDepth(getDepth() + 1);

				closeOpeningTag();
				// close opening tag before we write child elements
				node.open(mOut);
				mHasChildren = true;
				break;

			case STATE_START_TAG_CLOSED:
				if (mOpenChild != null)
				{
					mOpenChild.close();
				}
				mOpenChild = node;

				// prepare child
				node.setNamespaceRegistry(mNamespaceRegistry);
				node.setDepth(getDepth() + 1);

				node.open(mOut);
				mHasChildren = true;
				break;

			default:
				throw new InvalidStateException("can not add child - closing tag already written");
		}
		return this;
	}


	/**
	 * Add an {@link XmlAttribute} to this node.
	 * 
	 * If the node has not been opened for writing yet, the attribute is cached.
	 * 
	 * If this node has been opened for writing and no child has been written yet the attribute is written immediately.
	 * 
	 * If at least one child has been written yet or the node is closed an Exception is thrown.
	 * 
	 * @param attr
	 *            The {@link XmlAttribute}.
	 * @return This XmlTag instance.
	 * @throws IOException
	 * @throws InvalidStateException
	 *             if the attribute can't be written because the start tag has already been closed (because another node has been added).
	 * @throws InvalidValueException
	 * 
	 */
	public final XmlTag add(XmlAttribute attr) throws IOException, InvalidStateException, InvalidValueException
	{
		switch (state)
		{
			case STATE_NEW:
				// this node is not open yet -> cache the attribute

				// create cache if necessary.
				if (mAttributes == null)
				{
					mAttributes = new HashSet<XmlAttribute>();
				}
				else
				{
					// remove the attribute to ensure the new value gets added (see XmlAttribute for the reason}
					mAttributes.remove(attr);
				}

				// register any namespaces
				if (mNamespaceRegistry != null)
				{
					attr.setNamespaceRegistry(mNamespaceRegistry);
				}

				// cache the attribute
				mAttributes.add(attr);
				break;

			case STATE_START_TAG_OPEN:
				// the node is open and the start tag has not been closed yet -> write out immediately.

				// we still cache the attribute to ensure we don't write any attribute twice.
				if (mAttributes == null)
				{
					mAttributes = new HashSet<XmlAttribute>();
				}
				attr.setNamespaceRegistry(mNamespaceRegistry);
				if (mAttributes.add(attr))
				{
					mOut.write(' ');
					attr.write(mOut);
				}
				break;

			default:
				throw new InvalidStateException("can not add attribute - start tag already closed");
		}
		return this;
	}


	/**
	 * Add an IXmlTagSerializable child to this node.
	 * 
	 * @param serializable
	 *            An instance implementing {@link IXmlTagSerializable}.
	 * @return This XmlTag instance.
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public final XmlTag add(IXmlTagSerializable serializable) throws InvalidStateException, IOException, InvalidValueException
	{
		add(new XmlTagSerializableAdapter(serializable));
		return this;
	}


	/**
	 * Add an IXmlTextSerializable child to this node.
	 * 
	 * @param serializable
	 *            An instance implementing {@link IXmlTextSerializable}.
	 * @return This XmlTag instance.
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public final XmlTag add(IXmlTextSerializable serializable) throws InvalidStateException, IOException, InvalidValueException
	{
		add(new XmlTextSerializableAdapter(serializable));
		return this;
	}


	/**
	 * Add an IXmlAttributeSerializable child to this node.
	 * 
	 * @param serializable
	 *            An instance implementing {@link IXmlAttributeSerializable}.
	 * @return This XmlTag instance.
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public final XmlTag add(IXmlAttributeSerializable serializable) throws InvalidStateException, IOException, InvalidValueException
	{
		add(new XmlAttributeSerializableAdapter(serializable));
		return this;
	}


	/**
	 * Convenience method to add a text node.
	 * 
	 * @param text
	 *            A {@link String} with the text of the node.
	 * @return This XmlTag instance.
	 * @throws InvalidStateException
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	public final XmlTag addText(String text) throws InvalidStateException, IOException, InvalidValueException
	{
		add(new XmlText(text));
		return this;
	}


	/**
	 * Convenience method to add an attribute without namespace.
	 * 
	 * @param name
	 *            A {@link String} with the name of the attribute.
	 * @param value
	 *            A {@link String} with the attribute's value.
	 * @return This XmlTag instance.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	public final XmlTag addAttribute(String name, String value) throws IOException, InvalidStateException, InvalidValueException
	{
		add(new XmlAttribute(name, value));
		return this;
	}


	/**
	 * Open the tag an start writing it to the {@link Writer} {@code out}.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 */
	final void open(Writer out) throws IOException, InvalidStateException, InvalidValueException
	{
		if (state != STATE_NEW)
		{
			throw new InvalidStateException("can not open tag in state " + state);
		}

		mOut = out;
		out.write('<');
		if (mNamespace != null && mNamespace.hasPrefix)
		{
			mNamespace.writePrefix(out);
			out.write(':');
			out.write(mTagName);
		}
		else if (mNamespace != null)
		{
			out.write(mTagName);
			out.write(' ');
			mNamespace.write(out);
		}
		else
		{
			out.write(mTagName);
		}

		state = STATE_START_TAG_OPEN;

		writeAttributes(out, mAttributes);
		if (mHasChildren)
		{
			closeOpeningTag();
			mOpenChild = writeChildren(out, mChildren, mOpenChild);
		}
	}


	/**
	 * Close the tag.
	 */
	final void close() throws InvalidStateException, IOException, InvalidValueException
	{
		switch (state)
		{
			case STATE_NEW:
				throw new InvalidStateException("can not close tag - not yet opened");

			case STATE_START_TAG_OPEN:
				// the opening tag is still open, no children have been added

				writeNamespaces(mOut, mNamespaceRegistry.getNamespaces(getDepth()));
				mOut.write("/>");

				state = STATE_CLOSED;
				mNamespaceRegistry.clear(getDepth());
				break;

			case STATE_START_TAG_CLOSED:
				// close the last open child if any
				if (mOpenChild != null)
				{
					mOpenChild.close();
					mOpenChild = null;
				}
				// free child list
				mChildren = null;

				mOut.write("</");
				if (mNamespace != null && mNamespace.hasPrefix)
				{
					mNamespace.writePrefix(mOut);
					mOut.write(':');
				}
				mOut.write(mTagName);
				mOut.write('>');
				state = STATE_CLOSED;
				mNamespaceRegistry.clear(getDepth());
				break;

			default:
				throw new InvalidStateException("can not close tag - already closed");
		}
	}


	/**
	 * Write out attributes.
	 * 
	 * @param out
	 *            The {@link Writer} to write to.
	 * @param attributes
	 *            A {@link Set} of {@link XmlAttribute}s to write
	 * 
	 * @throws IOException
	 */
	private final static void writeAttributes(Writer out, Set<XmlAttribute> attributes) throws IOException
	{
		if (attributes != null && attributes.size() > 0)
		{
			for (XmlAttribute attr : attributes)
			{
				out.write(' ');
				attr.write(out);
			}
		}
	}


	/**
	 * Close the opening tag.
	 * <p>
	 * Writes out all namespaces registered at this depth first.
	 * </p>
	 * 
	 * @throws InvalidStateException
	 * @throws IOException
	 */
	private final void closeOpeningTag() throws InvalidStateException, IOException
	{
		if (state == STATE_START_TAG_OPEN)
		{
			Set<XmlNamespace> namespaces = mNamespaceRegistry.getNamespaces(getDepth());
			if (namespaces != null)
			{
				writeNamespaces(mOut, namespaces);
			}
			// lock this level the start tag is closed now, no new namespaces can be added
			mNamespaceRegistry.lock(getDepth());
			mOut.write('>');
			state = STATE_START_TAG_CLOSED;
		}
		else
		{
			throw new InvalidStateException("Can not close opening tag in state " + state);
		}
	}


	/**
	 * Write a list of children elements to {@code outputStream}. No Checks are performed to ensure the correct state!
	 * 
	 * The last written child is left open and returned.
	 * 
	 * @param outputStream
	 *            The {@link OutputStream} to write to.
	 * @param children
	 *            A {@link List} of {@link XmlAbstractNode}s.
	 * @param lastChild
	 *            The last child written so far or null if no child has been written yet.
	 * @return The last written child.
	 * @throws IOException
	 * @throws InvalidValueException
	 */
	private static XmlAbstractNode writeChildren(Writer out, List<XmlAbstractNode> children, XmlAbstractNode lastChild) throws InvalidStateException,
		IOException, InvalidValueException
	{
		if (children == null || children.size() == 0)
		{
			// no children, nothing to do
			return lastChild;
		}

		XmlAbstractNode last = lastChild;
		if (children != null)
		{
			for (XmlAbstractNode node : children)
			{
				if (last != null)
				{
					last.close();
				}
				last = node;
				node.open(out);

			}
			children.clear();
		}
		return last;
	}


	/**
	 * Write a list of namespaces to {@code outputStream}. No Checks are performed to ensure the correct state!
	 * 
	 * @param out
	 *            The {@link OutputStream} to write to.
	 * @param namespaceList
	 * @throws IOException
	 */
	private static void writeNamespaces(Writer out, Collection<XmlNamespace> namespaceList) throws IOException
	{
		if (namespaceList != null && namespaceList.size() > 0)
		{
			for (XmlNamespace ns : namespaceList)
			{
				out.write(' ');
				ns.write(out);
			}
		}
	}


	/**
	 * Set the {@link XmlNamespaceRegistry}.
	 * 
	 * This method passes {@code namespaceRegistry} to all children and attributes of this element.
	 * 
	 * @param namespaceRegistry
	 *            The {@link XmlNamespaceRegistry}.
	 * @throws InvalidValueException
	 */
	final void setNamespaceRegistry(XmlNamespaceRegistry namespaceRegistry) throws InvalidValueException
	{
		mNamespaceRegistry = namespaceRegistry;

		if (mNamespace == null && mNamespaceString != null && mNamespaceString.length() > 0)
		{
			mNamespace = mNamespaceRegistry.getNamespace(mNamespaceString);
		}

		if (mChildren != null)
		{
			for (XmlAbstractNode node : mChildren)
			{
				node.setNamespaceRegistry(namespaceRegistry);
			}
		}

		if (mAttributes != null)
		{
			for (XmlAttribute attr : mAttributes)
			{
				attr.setNamespaceRegistry(namespaceRegistry);
			}
		}
	}
}
