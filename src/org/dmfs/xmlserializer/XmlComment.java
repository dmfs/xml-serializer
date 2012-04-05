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
import java.util.ArrayList;
import java.util.List;


/**
 * An XML comment node.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class XmlComment extends XmlAbstractNode
{
	/**
	 * The first text segment of this comment node. Subsequently added text segments are appended to mCommentList or immediately written (depending on current
	 * state).
	 */
	private String mComment;

	/**
	 * A {@link List} of {@link String}s containing the cached text of this node.
	 */
	private List<String> mCommentList;

	/**
	 * The writer to write to.
	 */
	private Writer mOut;


	/**
	 * Constructor for an XmlComment node.
	 * 
	 * @param comment
	 *            The comment of this node (may be {@code null}).
	 * @throws InvalidValueException
	 */
	public XmlComment(String comment) throws InvalidValueException
	{
		if (comment != null && comment.contains("--"))
		{
			// -- is not allowed in XML comments
			throw new InvalidValueException("Xml comments must not contain \"--\"!");
		}
		mComment = comment;
	}


	/**
	 * Constructor for an XmlComment node without initial comment text.
	 * 
	 * @param comment
	 *            The comment of this node (may be {@code null}).
	 * @throws InvalidValueException
	 */
	public XmlComment() throws InvalidValueException
	{
		mComment = null;
	}


	/**
	 * Add text to this node.
	 * 
	 * @param comment
	 *            The text to append.
	 * @return This XmlText instance.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	public XmlComment add(String comment) throws IOException, InvalidStateException, InvalidValueException
	{
		switch (state)
		{
			case STATE_NEW:
				if (comment != null && comment.length() > 0)
				{
					if (comment.contains("--"))
					{
						// -- is not allowed in XML comments
						throw new InvalidValueException("Xml comments must not contain \"--\"!");
					}

					if (mCommentList == null)
					{
						mCommentList = new ArrayList<String>();
					}
					mCommentList.add(comment);
				}
				break;
			case STATE_START_TAG_OPEN:
			case STATE_START_TAG_CLOSED:
				if (comment != null && comment.length() > 0)
				{
					if (comment.contains("--"))
					{
						// -- is not allowed in XML comments
						throw new InvalidValueException("Xml comments must not contain \"--\"!");
					}

					mOut.write(comment);
				}
				break;
			case STATE_CLOSED:
				throw new InvalidStateException("Can not add comment - node already closed!");
		}
		return this;
	}


	@Override
	final void open(Writer out) throws InvalidStateException, IOException
	{
		if (state != STATE_NEW)
		{
			throw new InvalidStateException("Can not open comment node - it's already open!");
		}
		state = STATE_START_TAG_CLOSED;
		mOut = out;
		mOut.write("<!--");

		// write initial comment, if any
		if (mComment != null)
		{
			out.write(mComment);
		}

		// write cached comments if any
		if (mCommentList != null)
		{
			for (String comment : mCommentList)
			{
				out.write(comment);
			}
			// free mCommentList
			mCommentList = null;
		}
	}


	@Override
	final void close() throws IOException, InvalidStateException
	{
		if (state != STATE_START_TAG_CLOSED)
		{
			throw new InvalidStateException("Can not close comment note - it's not open!");
		}
		mOut.write("-->");

		state = STATE_CLOSED;
	}


	@Override
	final void setNamespaceRegistry(XmlNamespaceRegistry namespaceRegistry)
	{
		// nothing to do
	}

}
