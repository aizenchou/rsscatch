package controllers.util;

import java.io.*;
import java.util.*;

import models.Channel;
import models.Item;

import org.jdom2.*;
import org.jdom2.output.*;

import com.google.appengine.api.datastore.Blob;

public class RSSCreator {
/*	public static String AtomXMLtoString(Channel channel) {
		Element root = new Element("feed");
		// root.addNamespaceDeclaration(Namespace.getNamespace("http://www.w3.org/2005/Atom"));
		Document doc = new Document(root);
		Element feedTitle = new Element("title");
		feedTitle.setAttribute("type", "text");
		feedTitle.setText(channel.title);
		root.addContent(feedTitle);
		root.addContent(new Element("updated").setText(channel.lastBuildDate));
		root.addContent(new Element("link").setAttribute("href", channel.link));
		Element feedsub = new Element("subtitle");
		feedsub.setAttribute("type", "text");
		feedsub.setText(channel.description);
		root.addContent(feedsub);
		root.addContent(new Element("generator").setText(channel.generator));

		if (channel.items != null && channel.items.size() != 0)
			for (Item item : channel.items) {
				Element itemElement = new Element("entry");
				Element content = new Element("content");
				content.addContent(new CDATA(item.content));
				content.setAttribute("type", "html");
				Element title = new Element("title");
				title.setAttribute("type", "text");
				title.setText(item.title);
				itemElement.addContent(title);
				itemElement.addContent(new Element("id").setText(item.link));
				itemElement.addContent(new Element("updated")
						.setText(item.pubDate));
				itemElement.addContent(new Element("published")
						.setText(item.pubDate));
				Element author = new Element("author");
				author.addContent(new Element("name").setText(item.author));
				author.addContent(new Element("uri").setText(""));
				itemElement.addContent(author);
				Element summary = new Element("summary");
				summary.setAttribute("type", "text");
				summary.setText(item.description);
				itemElement.addContent(summary);
				itemElement.addContent(content);
				root.addContent(itemElement);
			}
		XMLOutputter XMLOut = new XMLOutputter(Format.getPrettyFormat());
		return XMLOut.outputString(doc);
	}
*/

	public static String RSSXMLtoString(Channel channel)
			throws FileNotFoundException, IOException {
		Element root = new Element("rss");
		root.setAttribute("version", "2.0");
		root.addNamespaceDeclaration(Namespace.getNamespace("content",
				"http://purl.org/rss/1.0/modules/content/"));
		root.addNamespaceDeclaration(Namespace.getNamespace("dc",
				"http://purl.org/dc/elements/1.1/"));
		root.addNamespaceDeclaration(Namespace.getNamespace("slash",
				"http://purl.org/rss/1.0/modules/slash/"));
		root.addNamespaceDeclaration(Namespace.getNamespace("atom",
				"http://www.w3.org/2005/Atom"));
		root.addNamespaceDeclaration(Namespace.getNamespace("wfw",
				"http://wellformedweb.org/CommentAPI/"));
		Document doc = new Document(root);
		Element chaElemt = new Element("channel");
		chaElemt.addContent(new Element("title").setText(channel.title));
		chaElemt.addContent(new Element("link").setText(channel.link));
		Element linkElemt = new Element("link", "atom",
				"http://www.w3.org/2005/Atom");
		linkElemt.setAttribute(new Attribute("href",
				"115.200.30.250:9000/catchjianshu.xml"));
		linkElemt.setAttribute(new Attribute("rel", "self"));
		linkElemt.setAttribute(new Attribute("type", "application/rss+xml"));
		chaElemt.addContent(linkElemt);
		chaElemt.addContent(new Element("description")
				.setText(channel.description));
		chaElemt.addContent(new Element("language").setText(channel.language));
		chaElemt.addContent(new Element("generator").setText(channel.generator));
		chaElemt.addContent(new Element("pubDate").setText(channel.pubDate));
		chaElemt.addContent(new Element("lastBuildDate")
				.setText(channel.lastBuildDate));
		if (channel.items != null && channel.items.size() != 0)
			for (Item item : channel.items) {
				Element itemElement = new Element("item");
				Element content = new Element("encoded", "content",
						"http://purl.org/rss/1.0/modules/content/");
				content.setAttribute(new Attribute("lang", "zh-CN"));
				content.addContent(new CDATA(item.content));
				// content.setText(new CDATA(item.content).toString());
				itemElement
						.addContent(new Element("title").setText(item.title));
				itemElement.addContent(new Element("link").setText(item.link));
				itemElement.addContent(new Element("guid").setText(item.link));
				itemElement.addContent(new Element("category")
						.setText(item.category));
				itemElement.addContent(new Element("author")
						.setText(item.author));
				itemElement.addContent(new Element("pubDate")
						.setText(item.pubDate));
				itemElement.addContent(new Element("description")
						.addContent(new CDATA(item.description)));
				itemElement.addContent(content);
				chaElemt.addContent(itemElement);
			}
		root.addContent(chaElemt);
		XMLOutputter XMLOut = new XMLOutputter(Format.getPrettyFormat());
		// XMLOut.output(doc, new FileOutputStream("public/target.xml"));
		return SaveData.saveStringtoStore(XMLOut.outputString(doc),
				"jianshu");

	}
}
