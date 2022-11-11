package delete;

import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Donut;

public class CmdDonutDelete implements Command{
	
	private ApplicationModel AppModel;
	private Donut donut;
	
	public CmdDonutDelete(ApplicationModel appModel, Donut donut) {
		AppModel = appModel;
		this.donut = donut;
	}

	@Override
	public void execute() {
		AppModel.removeShape(donut);
	}

	@Override
	public void unexecute() {
		AppModel.addShape(donut);
	}

}
