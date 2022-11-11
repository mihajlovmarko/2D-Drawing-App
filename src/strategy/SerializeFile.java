package strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import modelViewController.ApplicationModel;
import shapes.Shape;

public class SerializeFile implements OptionChooser{
	
	private FileOutputStream fileOutputStream;
	private FileInputStream fileInputStream;
	private ApplicationModel AppModel;
	

	public SerializeFile(ApplicationModel appModel) {
		AppModel = appModel;
	}

	@Override
	public void saveFile(File file) {
		try {
			fileOutputStream = new FileOutputStream(file + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
			out.writeObject(AppModel.getAllShapes());
			out.close();
			fileOutputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void openFile(File file) {
		try {
			fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
	        AppModel.addMultipleShapes((ArrayList<Shape>) objectInputStream.readObject());
	        objectInputStream.close();
	        fileInputStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
