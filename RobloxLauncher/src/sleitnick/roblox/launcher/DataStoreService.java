package sleitnick.roblox.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class DataStoreService {
	
	private static final class Data implements Serializable {
		private static final long serialVersionUID = 1L;
		private final Map<String, Object> data = new HashMap<String, Object>();
	}
	
	private static final String DATA_DIR_NAME = "data";
	private static final String DATA_FILE_NAME = "data.ser";
	
	private static Data data = null;
	
	// Load data:
	static {
		File dataDir = new File(DATA_DIR_NAME);
		if (dataDir.isDirectory()) {
			File dataFile = new File(dataDir, DATA_FILE_NAME);
			try {
				FileInputStream fis = new FileInputStream(dataFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Object objData = ois.readObject();
				ois.close();
				fis.close();
				if (objData instanceof Data) {
					data = (Data)objData;
				}
			} catch (Exception e) {
			}
		} else {
			dataDir.mkdir();
		}
		if (data == null) {
			data = new Data();
		}
	}
	
	/**
	 * Set a value in the data store
	 * @param key {@link String} key
	 * @param value Value
	 */
	protected static void set(String key, Object value) {
		data.data.put(key, value);
	}
	
	/**
	 * Get value from given key
	 * @param key {@link String} key
	 * @return {@link Object} Value
	 */
	protected static Object get(String key) {
		return data.data.get(key);
	}
	
	/**
	 * Save the data in the data store
	 */
	protected static void save() {
		File dataDir = new File(DATA_DIR_NAME);
		if (!dataDir.isDirectory()) {
			dataDir.mkdir();
		}
		File dataFile = new File(dataDir, DATA_FILE_NAME);
		if (!dataFile.exists()) {
			try {
				dataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(dataFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
