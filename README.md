# xml-serializer

__A lightweight & convenient XML serializer__

This is the XML serializer used by CalDAV-Sync & CardDAV-Sync for Android. It is aims to be more convenient than the stock Android XmlSerializer interface, still being somewhat lightweight and efficient.

This library features basic XML serialization. At present it does not support default namespaces and pretty printing. It will take care of XML entity encoding in texts and attribute values, but it won't check validity of tag and attribute names (you have to take care of this yourself). Currently only XML 1.0 and UTF-8 character set are supported.

## Requirements

Although this library has been developed for Android projects it does not have any Android dependencies, only Java standard packages are used.

## Example code

### Basic use case

This is a common use case that shows how to add nodes on the fly and also insert pre-build XML sub-trees (the shelves with books).

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("Library");
		// start serializer
		s.serialize(root);
		// add more tags
		root.add(new XmlTag("Shelf").add(new XmlTag("Book").add(new XmlAttribute("title", "Title1"))));
		root.add(new XmlTag("Shelf").add(new XmlTag("Book").add(new XmlAttribute("title", "Title2"))));
		root.add(new XmlTag("Shelf").add(new XmlTag("Book").add(new XmlAttribute("title", "Title3"))));
		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

the result will be equivalent to

		<?xml version="1.0" encoding="utf-8" ?>
		<Library>
			<Shelf>
				<Book title="Title1" />
			</Shelf>
			<Shelf>
				<Book title="Title2" />
			</Shelf>
			<Shelf>
				<Book title="Title2" />
			</Shelf>
		</Library>

just without the new lines and indentation (i.e. all tags will be written to one long line).

You can use namespaces of course (just default namespaces are not supported, i.e. all namespaces will be prefixed):

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		root.add(new XmlTag("XmlLibrary", "Shelf").add(new XmlTag("XmlLibrary", "Book").add(new XmlAttribute("title", "Title1"))));
		root.add(new XmlTag("XmlLibrary", "Shelf").add(new XmlTag("XmlLibrary", "Book").add(new XmlAttribute("title", "Title2"))));
		root.add(new XmlTag("XmlLibrary", "Shelf").add(new XmlTag("XmlLibrary", "Book").add(new XmlAttribute("title", "Title3"))));
		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

the result will be equivalent to

		<?xml version="1.0" encoding="utf-8" ?>
		<A:Library xmlns:A="XmlLibrary">
			<A:Shelf>
				<A:Book title="Title1" />
			</A:Shelf>
			<A:Shelf>
				<A:Book title="Title2" />
			</A:Shelf>
			<A:Shelf>
				<A:Book title="Title2" />
			</A:Shelf>
		</A:Library>

### Sub-classing

You can sub-class the XmlTag class for even more convenient XML building (this example also shows the method `addAttribute(String, String)`, a short-cut for `add(new XmlAttribute(String, String))`:

		public class Shelf extends XmlTag
		{
			public Shelf()
			{
				super("XmlLibrary", "Shelf");
			}
		}

		public class Book extends XmlTag
		{
			public Book(String title)
			{
				super("XmlLibrary", "Book");
				addAttribute("title", title); // shortcut for: add(new XmlAttribute("title", title));
			}
		}

		...

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		root.add(new Shelf().add(new Book("Title1")));
		root.add(new Shelf().add(new Book("Title2")));
		root.add(new Shelf().add(new Book("Title3")));
		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

The result is the same as above. Of course, you can create a class for `Library` as well.

### XmlSerializable - interface

Often it's not possible to subclass `XmlTag` (since Java does not support multiple inheritance). In those cases you can use an interface for XML serialization:

		public class Book extends SomeAbstractBook implements IXmlTagSerializable
		{
			private String mTitle;	// the title attribute

			public Book(String title)
			{
				mTitle = title;
			}

			public String getXmlNamespace()
			{
				return "XmlLibrary";
			} 

			public String getXmlTagName()
			{
				return "Book";
			}

			public void populateXmlTag(XmlTag adapter)
			{
				adapter.addAttribute("title", mTitle);
			}
		}

		public class Shelf extends SomeAbstractShelf implements IXmlTagSerializable
		{
			private List<Book> mBookList = new ArrayList<Book>();

			public String getXmlNamespace()
			{
				return "XmlLibrary";
			} 

			public String getXmlTagName()
			{
				return "Shelf";
			}

			public void populateXmlTag(XmlTag adapter)
			{
				for (Book book:mBookList)
				{
					adapter.add(book);
				}
			}
		}


		...

		List<Shelf> mShelves = new ArrayList<Shelf>()

		...

		// add some shelves and books

		...

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		for (Shelf shelf:mShelves)
		{
			root.add(shelf); // every shelf will render itself and the books it contains to XML
		}
		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

Now the XML depends on the shelves and their books, of course.

Btw. you can also sub-class XmlAttribute and XmlText  and there are interfaces IXmlAttributeSerializable and IXmlTextSerializable which work just the same way as above.

### Registering namespaces

This library automatically creates a prefix for each used namespace. It tries to define the prefixes as early as possible. If you already know which namespaces you need, you can register them early.

Consider the following code:

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		XmlTag shelf = new XmlTag("XmlLibrary", "Shelf");
		root.add(shelf);

		shelf.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title1")));

		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

Since the start tag `Shelf` has already been written when `XmlBook` was used the first time, the result will be equivalent to:

		<?xml version="1.0" encoding="utf-8" ?>
		<A:Library xmlns:A="XmlLibrary">
			<A:Shelf xmlns:B="XmlBook">
				<B:Book title="Title1" />
			</A:Shelf>
		</A:Library>

The `XmlBook` namespace will be bound to `B` in every shelf.

To ensure `XmlBook` is defined in `Library` you can register the namespace early:


		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// register the namespace
		s.registerNamespace("XmlBook");

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		XmlTag shelf = new XmlTag("XmlLibrary", "Shelf");
		root.add(shelf);

		shelf.add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title1")));

		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

Now the result is equivalent to:

		<?xml version="1.0" encoding="utf-8" ?>
		<A:Library xmlns:A="XmlLibrary" xmlns:B="XmlBook">
			<A:Shelf>
				<B:Book title="Title1" />
			</A:Shelf>
		</A:Library>

Alternatively you can insert the shelf at once (like in the second example and without registering the namespace early):

		// create Writer for output
		sw = new StringWriter();
		// create XmlSerializer
		s = new XmlSerializer(sw);

		// create root node
		XmlTag root = new XmlTag("XmlLibrary", "Library");
		// start serializer
		s.serialize(root);
		// add more tags
		root.add(new XmlTag("XmlLibrary", "Shelf").add(new XmlTag("XmlBook", "Book").add(new XmlAttribute("title", "Title1"))));
		// finish serializer
		s.close();
		// get the result
		String xml = sw.toString();

The result will be the same as above (since the root start tag is still open when the new namespace is being registered).

## TODO:

* Check tag and attribute name validity
* Support CDATA
* Add Comment support
* Support more recent XML versions properly
* Write missing test cases, improve the existing tests

## License

Xml-serializer is licensed under GPL version 2 or newer (see `LICENSE`).

