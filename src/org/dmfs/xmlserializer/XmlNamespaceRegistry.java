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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * XmlNamespaceRegistry manages namespaces and keeps track of the depth they're defined in.
 * 
 * @author Marten Gajda <marten@dmfs.org>
 */
final class XmlNamespaceRegistry
{
	/**
	 * Namespace index.
	 */
	private final Map<String, XmlNamespace> mNamespaces = new HashMap<String, XmlNamespace>();

	/**
	 * List containing all namespaces per depth level.
	 */
	private final List<Set<XmlNamespace>> mNamespaceLevelMap = new ArrayList<Set<XmlNamespace>>();

	/**
	 * The first level that is available to add namespaces.
	 */
	private int mFirstUnlockedLevel = 0;

	/**
	 * Counter for the prefixes in use.
	 */
	private int mPrefixCounter = 0;


	/**
	 * Get an {@link XmlNamespace} object for the given namespace. Returns an existing namespace if any, creates a new object otherwise.
	 * 
	 * @param namespace
	 *            A {@link String} containing the namespace.
	 * @return An existing or new {@link XmlNamespace} instance.
	 * @throws InvalidValueException
	 */
	XmlNamespace getNamespace(String namespace) throws InvalidValueException
	{
		XmlNamespace ns;
		if ((ns = mNamespaces.get(namespace)) != null)
		{
			return ns;
		}

		return createNamespace(namespace);
	}


	/**
	 * Lock an XML depth level for new namespaces. No new namespaces will be added to levels equal or below of {@code depth}
	 * 
	 * @param depth
	 *            The XML element depth level to lock for new namespaces.
	 */
	void lock(int depth)
	{
		mFirstUnlockedLevel = Math.max(depth + 1, mFirstUnlockedLevel);
	}


	/**
	 * Remove all namespace definitions in levels above or equal to {@code depth}.
	 * 
	 * @param depth
	 *            The fist level to clear.
	 */
	void clear(int depth)
	{
		int size = mNamespaceLevelMap.size();
		// remove all namespaces in all levels to depthLevel starting at size-1
		for (int i = size - 1; i >= depth; --i)
		{
			Set<XmlNamespace> namespaces = mNamespaceLevelMap.get(i);
			if (namespaces != null)
			{
				// remove all namespaces at this depth
				for (XmlNamespace ns : namespaces)
				{
					mNamespaces.remove(ns.namespace);
				}
				// clear set instead of removing it, we can use it again
				namespaces.clear();
			}
		}
		mFirstUnlockedLevel = Math.min(depth, mFirstUnlockedLevel);
	}


	/**
	 * Returns a set of namespaces defined at a specific depth. The result may be null or empty.
	 * 
	 * @param deoth
	 *            The level of interest.
	 * @return A {@link Set} of {@link XmlNamespace}s or null if no namespaces are defined at this depth.
	 */
	Set<XmlNamespace> getNamespaces(int deoth)
	{
		if (mNamespaceLevelMap.size() > deoth)
		{
			return mNamespaceLevelMap.get(deoth);

		}
		else
		{
			return null;
		}
	}


	/**
	 * Clear this registry and reset prefixes.
	 */
	void clear()
	{
		clear(0);
		mPrefixCounter = 0;
	}


	/**
	 * Create a new {@link XmlNamespace} instance for {code namespace}.
	 * 
	 * @param namespace
	 * @return
	 * @throws InvalidValueException
	 */
	private XmlNamespace createNamespace(String namespace) throws InvalidValueException
	{
		String prefix = buildPrefix();

		XmlNamespace ns = new XmlNamespace(prefix, namespace);

		if (prefix != null)
		{
			// add namespace to namespace cache
			mNamespaces.put(namespace, ns);
			// add to the first unlocked level
			Set<XmlNamespace> namespaces = getNamespaceSet(mFirstUnlockedLevel);
			namespaces.add(ns);
		}

		return ns;
	}


	/**
	 * Create a new prefix that has not been used yet.
	 * 
	 * TODO: properly handle the case when we're out of prefixes (even though this is extremely unlikely to happen)
	 * 
	 * @return A {link String} with a new prefix or null if we hit the limit.
	 */
	private String buildPrefix()
	{
		if (mPrefixCounter >= 26 * 26 * 26 * 26)
		{
			return null;
		}
		// make up a new prefix
		int count = ++mPrefixCounter;
		int pos = 0;
		final byte[] prefixBytes = new byte[4]; // don't support more than 26^4 prefixes
		do
		{
			prefixBytes[pos] = (byte) (count % 26 + 'A' - 1);
			count /= 26;
			++pos;
		} while (count > 0);

		return new String(prefixBytes, 0, pos);
	}


	/**
	 * Return a set of {@link XmlNamespace}s for the given {@code depth}. In contrast to {@link getNamespaces} this method never returns null.
	 * 
	 * @param depth
	 *            The depth of interest.
	 * 
	 * @return A {@link Set} of {@link XmlNamespace}s defined at this depth.
	 */
	private Set<XmlNamespace> getNamespaceSet(int depth)
	{
		int size = mNamespaceLevelMap.size();

		// fill intermediate levels with null
		while (size < depth)
		{
			mNamespaceLevelMap.add(null);
			++size;
		}

		Set<XmlNamespace> set;
		if (size <= depth || (set = mNamespaceLevelMap.get(depth)) == null)
		{
			// no Set exists yet, create one
			set = new HashSet<XmlNamespace>();
			mNamespaceLevelMap.add(depth, set);
		}
		return set;
	}
}
