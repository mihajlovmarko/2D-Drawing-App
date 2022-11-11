package strategy;

import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import modelViewController.ApplicationFrame;

public class SerializeDrawing implements OptionChooser{

	private ApplicationFrame AppFrame;

	public SerializeDrawing(ApplicationFrame appFrame) {
		AppFrame = appFrame;
	}

	@Override
	public void saveFile(File file) {
		BufferedImage imageBuffer = null;
		try {
			imageBuffer = new Robot().createScreenCapture(AppFrame.getAppView().getBounds());
			AppFrame.getAppView().paint(imageBuffer.createGraphics());
			ImageIO.write(imageBuffer,"jpeg", new File(file + ".jpeg"));
		} catch (Exception e) {
			System.out.println(e.getMessage());

		}
	}

	@Override
	public void openFile(File file) {
		// TODO Auto-generated method stub

	}

}
