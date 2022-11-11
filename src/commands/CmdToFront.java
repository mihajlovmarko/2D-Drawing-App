package commands;

import java.util.Collections;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Shape;

public class CmdToFront implements Command {

	private ApplicationModel AppModel;
	private int index;
	private Shape shape;
	public CmdToFront(ApplicationModel appModel, int index, Shape shape) {
		this.AppModel = appModel;
		this.index = index;
		this.shape = shape;
	}

	@Override
	public void execute() {
		if(index!=AppModel.getShapes().size()-1) {
			Collections.swap(AppModel.getShapes(), index+1, index);
		}	
	}

	@Override
	public void unexecute() {
		if(index!=AppModel.getShapes().size()-1) {
			Collections.swap(AppModel.getShapes(), index, index+1);
		}
	}
	
	@Override
	public String toString() {
		return "Moved to front->" + shape.toString();
	}


}
