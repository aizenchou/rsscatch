package models;

import java.util.Vector;

public class Channel {
	public String title;
	public String link;
	public String description;
	public String language;
	public String generator;
	public String pubDate;
	public String lastBuildDate;
	public Vector<Item> items;

	public Channel(String title, String link, String description,
			String language, String generator, String pubDate,
			String lastBuildDate, Vector<Item> items) {
		super();
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.generator = generator;
		this.pubDate = pubDate;
		this.lastBuildDate = lastBuildDate;
		this.items = items;
	}

}
