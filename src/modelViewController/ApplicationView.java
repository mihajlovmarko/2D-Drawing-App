package modelViewController;
//
import java.awt.Graphics;
import java.util.ListIterator;

import javax.swing.JPanel;

import shapes.Shape;

public class ApplicationView extends JPanel{
	public ApplicationView() {

	}
	ApplicationModel AppModel = new ApplicationModel();

	public ApplicationModel getAppModel() {
		return AppModel;
	}

	public void setAppModel(ApplicationModel appModel) {
		AppModel = appModel;
	}

	public void paint(Graphics g) {
		super.paint(g);
		ListIterator<Shape> it = AppModel.getShapes().listIterator();

		while(it.hasNext()) {
			it.next().draw(g);
		}
	}
}
