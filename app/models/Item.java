package models;

import java.util.Vector;

public class Item {
	public String title;
	public String link;
	public String category;
	public String author;
	public String description;
	public String pubDate;
	public String content;

	public Item(String title, String link, String category, String author,
			String description, String pubDate, String content) {
		super();
		this.title = title;
		this.link = link;
		this.category = category;
		this.author = author;
		this.description = description;
		this.pubDate = pubDate;
		this.content = content;
	}

}
