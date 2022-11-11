package delete;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Line;

public class CmdLineDelete implements Command{
	private ApplicationModel AppModel;
	private Line line;
	
	 
	public CmdLineDelete(ApplicationModel appModel, Line line) {
		AppModel = appModel;
		this.line = line;
	}

	@Override
	public void execute() {
//		AppModel.getShapes().remove(line);
		AppModel.removeShape(line);
	} 

	@Override
	public void unexecute() {
		AppModel.addShape(line);
	}

}
