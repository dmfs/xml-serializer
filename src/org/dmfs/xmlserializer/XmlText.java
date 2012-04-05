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
 * An XML text node.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class XmlText extends XmlAbstractNode
{
	/**
	 * The first text segment of this node. Subsequently added text segments are appended to mTextList or immediately written (depending on current state).
	 */
	private String mText;

	/**
	 * A {@link List} of {@link String}s containing the cached text of this node.
	 */
	private List<String> mTextList;

	/**
	 * The writer to write to.
	 */
	private Writer mOut;


	/**
	 * Constructor for an XmlText node.
	 * 
	 * @param text
	 *            The text of this node (may be {@code null}).
	 */
	public XmlText(String text)
	{
		mText = text;
	}


	/**
	 * Add text to this node.
	 * 
	 * @param text
	 *            The text to append.
	 * @return This XmlText instance.
	 * @throws IOException
	 * @throws InvalidStateException
	 */
	public XmlText add(String text) throws IOException, InvalidStateException
	{
		switch (state)
		{
			case STATE_NEW:
				if (text != null && text.length() > 0)
				{
					if (mTextList == null)
					{
						mTextList = new ArrayList<String>();
					}
					mTextList.add(text);
				}
				break;
			case STATE_START_TAG_OPEN:
			case STATE_START_TAG_CLOSED:
				if (text != null && text.length() > 0)
				{
					XmlUtils.writeXmlEntityEncodedString(mOut, text);
				}
				break;
			case STATE_CLOSED:
				throw new InvalidStateException("Can not add text - node already closed!");
		}
		return this;
	}


	@Override
	final void open(Writer out) throws InvalidStateException, IOException
	{
		if (state != STATE_NEW)
		{
			throw new InvalidStateException("Can not open text node - it's already open!");
		}
		state = STATE_START_TAG_CLOSED;
		mOut = out;

		// write initial text, if any
		if (mText != null)
		{
			XmlUtils.writeXmlEntityEncodedString(out, mText);
		}

		// write cached text if any
		if (mTextList != null)
		{
			for (String text : mTextList)
			{
				XmlUtils.writeXmlEntityEncodedString(out, text);
			}
			// free mTextList
			mTextList = null;
		}
	}


	@Override
	final void close() throws IOException, InvalidStateException
	{
		if (state != STATE_START_TAG_CLOSED)
		{
			throw new InvalidStateException("Can not close text note - it's not open!");
		}

		state = STATE_CLOSED;
	}


	@Override
	final void setNamespaceRegistry(XmlNamespaceRegistry namespaceRegistry)
	{
		// nothing to do
	}

}
