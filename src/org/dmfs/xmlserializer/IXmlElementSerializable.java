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


/**
 * Interface for classes that serialize to an XML element.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface IXmlElementSerializable
{
	/**
	 * Get the element's namespace.
	 * 
	 * @return A {@link String} containing the namespace or {@code null} if the element has no namespace.
	 */
	public String getXmlNamespace();


	/**
	 * Get the element's name.
	 * 
	 * @return A {@link String} containing the name. Must not be {@code null}!
	 */
	public String getXmlElementName();


	/**
	 * Populate the element with attributes and children.
	 * <p>
	 * If this element has any attributes or children add them to {@code adapter}.
	 * </p>
	 * 
	 * @param adapter
	 *            The adapter where to add attributes and children.
	 * @throws IOException
	 * @throws InvalidStateException
	 * @throws InvalidValueException
	 */
	public void populateXmlElement(XmlElement adapter) throws IOException, InvalidStateException, InvalidValueException;
}
