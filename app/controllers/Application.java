package controllers;

import play.*;
import play.mvc.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.appengine.api.datastore.EntityNotFoundException;

import controllers.util.RSSCreator;
import controllers.util.SaveData;
import models.*;

public class Application extends Controller {

	final static String INDEX_REG_STRING = "<a href=\"(/p/[a-z0-9A-Z]{10,15})\" class=\"title\" target=\"_blank\">";
	final static String ITEM_TITLE_REG_STRING = "<h1 class=\"title\">([\\s\\S]*?)</h1>";
	final static String ITEM_CATE_REG_STRING = "<i class=\"icon-book\"></i>([\\s\\S]*?)</a>";
	final static String ITEM_AUTHOR_REG_STRING = "<div class=\"avatar\"([\\s\\S]*?)<span>([\\s\\S]*?)</span>";
	final static String ITEM_DESC_REG_STRING = "<div id=\"short-abbr\" class=\"hide\">([\\s\\S]*?)</div>";
	final static String ITEM_CONTENT_REG_STRING = "<div class=\"show-content\">([\\s\\S]*?)</div>";
	final static String ITEM_PUBDATE_REG_STRING = "<i class=\"icon-book\">([\\s\\S]*?)</a>([\\s\\S]*?)<span class=\"wordage\">";
	final static String DATE_FORMAT_STRING = "yyyy-MM-dd hh:mm:ss";

	public static void index() {
		render();
	}

	public static String catchData(String URL) throws Exception {
		URL url = new URL(URL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		InputStreamReader input = new InputStreamReader(
				httpConn.getInputStream(), "utf-8");
		BufferedReader bufReader = new BufferedReader(input);
		String line = "";
		StringBuilder contentBuf = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			contentBuf.append(line);
		}
		return contentBuf.toString();
	}

	public static Vector<String> getMatchStrings(String regex,
			String dataresult, int groupnum) {
		Vector<String> matchStrings = new Vector<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dataresult);
		while (matcher.find())
			matchStrings.add(matcher.group(groupnum));
		return matchStrings;
	}

	public static String getStringFromText(String filename) throws IOException {
		File txt = Play.getFile("public/" + filename);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(
				txt));
		BufferedReader bufReader = new BufferedReader(reader);
		String result = "";
		String lineTxt = null;
		while ((lineTxt = bufReader.readLine()) != null) {
			result = result += lineTxt;
		}
		bufReader.close();
		return result;
	}

	public static void catchjianshu() throws Exception {
		String url = "http://jianshu.io/";
		String result = catchData(url);
		// String result = getStringFromText("jianshu.txt");
		Vector<Item> items = new Vector<>();
		for (String tabString : getMatchStrings(INDEX_REG_STRING, result, 1)) {
			String pageURL = "http://jianshu.io" + tabString;
			System.out.println(pageURL);
			// String pageText = getStringFromText("page.txt");
			String pageText = catchData(pageURL);
//			System.out.println(getMatchStrings(ITEM_TITLE_REG_STRING, pageText,
//					1).get(0));
//			System.out.println(getMatchStrings(ITEM_CATE_REG_STRING, pageText,
//					1).get(0));
//			System.out.println(getMatchStrings(ITEM_AUTHOR_REG_STRING,
//					pageText, 2).get(0));
//			System.out.println(getMatchStrings(ITEM_DESC_REG_STRING, pageText,
//					1));
			items.add(new Item(getMatchStrings(ITEM_TITLE_REG_STRING, pageText,1).get(0), 
					pageURL, 
					getMatchStrings(ITEM_CATE_REG_STRING,pageText, 1).get(0), 
					getMatchStrings(ITEM_AUTHOR_REG_STRING, pageText, 2).get(0),
					getMatchStrings(ITEM_DESC_REG_STRING, pageText, 1).get(0),
					getMatchStrings(ITEM_PUBDATE_REG_STRING, pageText, 2).get(0), 
					getMatchStrings(ITEM_CONTENT_REG_STRING,pageText, 1).get(0).replace((char)8, ' ')));
			
		}
		Channel channel = new Channel("简书", "http://jianshu.io", "找回文字的力量", "zh-CN", "aizenchou", 
				new java.text.SimpleDateFormat(DATE_FORMAT_STRING).format(new Date()), 
				new java.text.SimpleDateFormat(DATE_FORMAT_STRING).format(new Date()), items);
		//getrss(channel);
		renderXml(RSSCreator.RSSXMLtoString(channel));
		//renderXml("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<feed xmlns=\"http://www.w3.org/2005/Atom\">"+RSSCreator.AtomXMLtoString(channel).substring(46));
	}
	
	public static void printxml() throws EntityNotFoundException {
		renderXml(SaveData.loadStringfromStore("jianshu"));
	}
}