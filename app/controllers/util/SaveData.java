package controllers.util;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Text;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.IOException;
import java.util.Date;

public class SaveData {
	public static String saveStringtoStore(String data,String dataKey) {
		Key key = KeyFactory.createKey("string", dataKey);
		Entity dataEntity = new Entity(key);
		dataEntity.setProperty("rssdata", new Text(data));
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(dataEntity);
		return data;
	}
	
	public static String loadStringfromStore(String datakey) throws EntityNotFoundException {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		Key key = KeyFactory.stringToKey(datakey);
		Key key = KeyFactory.createKey("string", datakey);
		Entity dataEntity = datastore.get(key);
		Text xmlText =  (Text)dataEntity.getProperty("rssdata");
		return xmlText.getValue();
	}
}
