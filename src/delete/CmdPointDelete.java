package delete;



import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Point;

public class CmdPointDelete implements Command {

	private ApplicationModel AppModel;
	private Point point;
	
	public CmdPointDelete(ApplicationModel appModel, Point point) {
		AppModel = appModel;
		this.point = point;
	}
 
	@Override
	public void execute() {
//		AppModel.getShapes().remove(point);
		AppModel.removeShape(point);
		  
	}
 
	@Override
	public void unexecute() {
		AppModel.addShape(point);
	}

}
