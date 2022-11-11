package delete;

import adapters.HexagonAdapter;
import modelViewController.ApplicationModel;
import shapes.Command;

public class CmdHexagonDelete implements Command{
	
	private ApplicationModel AppModel;
	private HexagonAdapter hexagon;
	
	
	
	public CmdHexagonDelete(ApplicationModel appModel, HexagonAdapter hexagon) {
		AppModel = appModel;
		this.hexagon = hexagon;
	}

	@Override
	public void execute() {
		AppModel.removeShape(hexagon);
	}

	@Override
	public void unexecute() {
		AppModel.addShape(hexagon);
	}

}
