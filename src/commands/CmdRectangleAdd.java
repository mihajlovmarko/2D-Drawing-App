package commands;
//
import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Rectangle;

public class CmdRectangleAdd implements Command {
	private ApplicationModel AppModel;
	private Rectangle rectangle;
	
	public CmdRectangleAdd(ApplicationModel appModel, Rectangle rectangle) {
		AppModel = appModel;
		this.rectangle = rectangle;
	}

	public void execute() {
		AppModel.addShape(rectangle);
	}

	@Override
	public void unexecute() {
		AppModel.removeShape(rectangle);
	}
	
	@Override
	public String toString() {
		return "Added->" + rectangle.toString();
	}

}
