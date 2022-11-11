package modelViewController;

import javax.swing.JFrame;

public class DrawingApplication {

	public static void main(String[] args) {
		ApplicationModel AppModel = new ApplicationModel();
		ApplicationFrame AppFrame = new ApplicationFrame();
		ApplicationController AppController = new ApplicationController(AppModel, AppFrame);
		AppFrame.getAppView().setAppModel(AppModel);
		AppFrame.setAppController(AppController);
		AppModel.addPropertyChangeListener(AppController);
		AppFrame.setSize(800,600);
		AppFrame.setTitle("Drawing Application");
		AppFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AppFrame.setVisible(true);

	}

}
