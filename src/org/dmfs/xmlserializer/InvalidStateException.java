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

/**
 * Exception that indicates that an XML element could not be written because the respective tag was in an invalid state for this operation.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
public class InvalidStateException extends Exception
{

	/**
	 * Auto generated serial.
	 */
	private static final long serialVersionUID = -4152120295503634884L;


	/**
	 * Constructor.
	 * 
	 * @param message
	 *            The reason for this Exception.
	 */
	public InvalidStateException(String message)
	{
		super(message);
	}
}
