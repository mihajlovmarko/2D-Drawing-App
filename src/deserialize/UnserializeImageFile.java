package deserialize;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class UnserializeImageFile implements Unserialize{

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Object> unserializeImgLog(String path) {
		try {
			FileInputStream file = new FileInputStream(path);
			@SuppressWarnings("resource")
			ObjectInputStream objInput = new ObjectInputStream(file);
			return (ArrayList<Object>) objInput.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
