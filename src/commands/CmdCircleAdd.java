package commands;
//
import modelViewController.ApplicationModel;
import shapes.Circle;
import shapes.Command;

public class CmdCircleAdd implements Command{
	ApplicationModel AppModel;
	Circle circle;
	
	public CmdCircleAdd(ApplicationModel appModel, Circle circle) {
		super();
		AppModel = appModel;
		this.circle = circle;
	}

	public void execute() {
		AppModel.addShape(circle);
	}

	@Override
	public void unexecute() {
		AppModel.removeShape(circle);
	}
	
	@Override
	public String toString() {
		return "Added->"+circle.toString();
	}

}
