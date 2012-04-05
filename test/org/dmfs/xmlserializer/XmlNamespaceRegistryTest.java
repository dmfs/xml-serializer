package org.dmfs.xmlserializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class XmlNamespaceRegistryTest
{

	private XmlNamespaceRegistry mNameSpaceRegistry;


	/**
	 * Create the XmlNamespaceRegistry used for all tests.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		mNameSpaceRegistry = new XmlNamespaceRegistry();
	}


	/**
	 * Test getNamespace().
	 * 
	 * @throws InvalidValueException
	 */
	@Test
	public void testGetNamespace() throws InvalidValueException
	{
		// create namespaces an check them
		XmlNamespace ns1 = mNameSpaceRegistry.getNamespace("A");
		assertEquals(new XmlNamespace("A", "A"), ns1);

		XmlNamespace ns2 = mNameSpaceRegistry.getNamespace("B");
		assertEquals(new XmlNamespace("B", "B"), ns2);

		XmlNamespace ns3 = mNameSpaceRegistry.getNamespace("X");
		assertEquals(new XmlNamespace("C", "X"), ns3);

		// namespace B was already defined so ns4 should be the same object as ns2
		XmlNamespace ns4 = mNameSpaceRegistry.getNamespace("B");
		assertSame(ns2, ns4);

		// create 26 more namespaces
		for (int i = 0; i < 26; ++i)
		{
			@SuppressWarnings("unused")
			XmlNamespace ns5to30 = mNameSpaceRegistry.getNamespace(Integer.toString(i));
		}

		// create another one check
		XmlNamespace ns31 = mNameSpaceRegistry.getNamespace("Y");
		assertEquals(new XmlNamespace("DA", "Y"), ns31);

		// get the first one again
		XmlNamespace ns32 = mNameSpaceRegistry.getNamespace("A");
		assertSame(ns1, ns32);

	}


	/**
	 * Test getNamesspaces().
	 * 
	 * @throws InvalidValueException
	 */
	@Test
	public void testGetNamespaces() throws InvalidValueException
	{
		// without namespaces the registry should return null for all levels
		assertNull(mNameSpaceRegistry.getNamespaces(0));
		assertNull(mNameSpaceRegistry.getNamespaces(1));
		assertNull(mNameSpaceRegistry.getNamespaces(2));

		// create some namespaces at level 0
		XmlNamespace ns1 = mNameSpaceRegistry.getNamespace("A");
		XmlNamespace ns2 = mNameSpaceRegistry.getNamespace("B");
		XmlNamespace ns3 = mNameSpaceRegistry.getNamespace("C");

		// check that the namespaces of level 0 are the ones we created
		Set<XmlNamespace> nsSet1 = mNameSpaceRegistry.getNamespaces(0);
		assertEquals(3, nsSet1.size());
		assertTrue(nsSet1.contains(ns1));
		assertTrue(nsSet1.contains(ns2));
		assertTrue(nsSet1.contains(ns3));
		// all other levels should return null
		assertNull(mNameSpaceRegistry.getNamespaces(1));
		assertNull(mNameSpaceRegistry.getNamespaces(2));

		// lock the first level and create additional namespaces
		mNameSpaceRegistry.lock(0);
		XmlNamespace ns4 = mNameSpaceRegistry.getNamespace("D");
		XmlNamespace ns5 = mNameSpaceRegistry.getNamespace("E");
		XmlNamespace ns6 = mNameSpaceRegistry.getNamespace("F");

		// check that the namespaces of level 0 are still the same
		Set<XmlNamespace> nsSet2 = mNameSpaceRegistry.getNamespaces(0);
		assertEquals(3, nsSet2.size());
		assertTrue(nsSet2.contains(ns1));
		assertTrue(nsSet2.contains(ns2));
		assertTrue(nsSet2.contains(ns3));
		// check that the namespaces of level 1 are the new ones
		Set<XmlNamespace> nsSet3 = mNameSpaceRegistry.getNamespaces(1);
		assertEquals(3, nsSet3.size());
		assertTrue(nsSet3.contains(ns4));
		assertTrue(nsSet3.contains(ns5));
		assertTrue(nsSet3.contains(ns6));
		// all other levels should return null
		assertNull(mNameSpaceRegistry.getNamespaces(2));
	}


	/**
	 * Test clear(int depth).
	 * 
	 * @throws InvalidValueException
	 */
	@SuppressWarnings("unused")
	@Test
	public void testClearDepth() throws InvalidValueException
	{
		// add some namespaces at different depths
		XmlNamespace ns1 = mNameSpaceRegistry.getNamespace("A");
		XmlNamespace ns2 = mNameSpaceRegistry.getNamespace("B");
		XmlNamespace ns3 = mNameSpaceRegistry.getNamespace("C");
		mNameSpaceRegistry.lock(0);
		XmlNamespace ns4 = mNameSpaceRegistry.getNamespace("D");
		XmlNamespace ns5 = mNameSpaceRegistry.getNamespace("E");
		mNameSpaceRegistry.lock(1);
		XmlNamespace ns6 = mNameSpaceRegistry.getNamespace("F");

		// clear the registry
		mNameSpaceRegistry.clear(1);

		// ensure level 0 is still the same and 1 and 2 are clean
		assertEquals(3, mNameSpaceRegistry.getNamespaces(0).size());
		Set<XmlNamespace> nsSet = mNameSpaceRegistry.getNamespaces(0);
		assertTrue(nsSet.contains(ns1));
		assertTrue(nsSet.contains(ns2));
		assertTrue(nsSet.contains(ns3));
		assertEquals(0, mNameSpaceRegistry.getNamespaces(1).size());
		assertEquals(0, mNameSpaceRegistry.getNamespaces(2).size());

		// add another namespace
		XmlNamespace ns7 = mNameSpaceRegistry.getNamespace("G");
		// ensure prefixes continue at the next prefix
		assertEquals(new XmlNamespace("G", "G"), ns7);

		// ensure level 1 is populated again
		assertEquals(1, mNameSpaceRegistry.getNamespaces(1).size());

	}
	
	/**
	 * Test clear().
	 * 
	 * @throws InvalidValueException
	 */
	@SuppressWarnings("unused")
	@Test
	public void testClear() throws InvalidValueException
	{
		// add some namespaces at different depths
		XmlNamespace ns1 = mNameSpaceRegistry.getNamespace("A");
		XmlNamespace ns2 = mNameSpaceRegistry.getNamespace("B");
		XmlNamespace ns3 = mNameSpaceRegistry.getNamespace("C");
		mNameSpaceRegistry.lock(0);
		XmlNamespace ns4 = mNameSpaceRegistry.getNamespace("D");
		XmlNamespace ns5 = mNameSpaceRegistry.getNamespace("E");
		mNameSpaceRegistry.lock(1);
		XmlNamespace ns6 = mNameSpaceRegistry.getNamespace("F");

		// clear the registry
		mNameSpaceRegistry.clear();

		// ensure all levels are clean
		assertEquals(0, mNameSpaceRegistry.getNamespaces(0).size());
		assertEquals(0, mNameSpaceRegistry.getNamespaces(1).size());
		assertEquals(0, mNameSpaceRegistry.getNamespaces(2).size());

		// add another namespace
		XmlNamespace ns7 = mNameSpaceRegistry.getNamespace("A");
		// ensure prefixes start at "A" again
		assertEquals(new XmlNamespace("A", "A"), ns7);

	}
}
