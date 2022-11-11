package commands;

import java.util.Collections;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdToBack implements Command{
	private ApplicationModel AppModel;
	private int index;
	private Shape shape;
	
	
	
	public CmdToBack(ApplicationModel appModel, int index, Shape shape) {
		AppModel = appModel;
		this.index = index;
		this.shape = shape;
	}

	@Override
	public void execute() {
		if(index!=0) { 
			Collections.swap(AppModel.getShapes(), index-1, index);
		}
	}

	@Override
	public void unexecute() {
		if(index!=0) {
			Collections.swap(AppModel.getShapes(), index, index-1);
		}
	}
	
	@Override
	public String toString() {
		return "Moved to back->" + shape.toString();
	}

}
