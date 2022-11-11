package commands;
//
import adapters.HexagonAdapter;
import modelViewController.ApplicationModel;
import shapes.Command;

public class CmdHexagonAdd implements Command {
	private ApplicationModel AppModel;
	HexagonAdapter hexagon;
	
	public CmdHexagonAdd(ApplicationModel appModel, HexagonAdapter hexagon) {
		super();
		AppModel = appModel;
		this.hexagon = hexagon;
	}

	public void execute() {
		AppModel.addShape(hexagon);
	}

	@Override
	public void unexecute() {
		AppModel.removeShape(hexagon);
	}
	
	@Override
	public String toString() {
		return "Added->"+hexagon.toString();
	}

}
