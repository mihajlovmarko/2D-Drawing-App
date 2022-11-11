package commands;
//
import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Donut;

public class CmdDonutAdd implements Command {
	ApplicationModel AppModel;
	Donut donut;
	
	public CmdDonutAdd(ApplicationModel appModel, Donut donut) {
		AppModel = appModel;
		this.donut = donut;
	}

	public void execute() {
		AppModel.addShape(donut);
	}

	@Override
	public void unexecute() {
		AppModel.removeShape(donut);
	}

	
	@Override
	public String toString() {
		return "Added->" + donut.toString();
	}
}
