package commands;
//
import modelViewController.ApplicationModel;
import shapes.Command;
import shapes.Line;

public class CmdLineAdd implements Command{
	private ApplicationModel AppModel;
	private Line line;
	
	public CmdLineAdd(ApplicationModel appModel, Line line) {
		AppModel = appModel;
		this.line = line;
	}
 
	@Override
	public void execute() {
		AppModel.addShape(line);
	}
 
	@Override
	public void unexecute() {
		AppModel.removeShape(line);
	}
	
	@Override
	public String toString() {
		return "Added->" + line.toString();
	}

}
