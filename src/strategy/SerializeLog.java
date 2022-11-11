package strategy;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.DefaultListModel;

import adapters.HexagonAdapter;
import commands.CmdBringToBack;
import commands.CmdBringToFront;
import commands.CmdCircleAdd;
import commands.CmdCircleUpdate;
import commands.CmdDonutAdd;
import commands.CmdDonutUpdate;
import commands.CmdHexagonAdd;
import commands.CmdHexagonUpdate;
import commands.CmdLineAdd;
import commands.CmdLineUpdate;
import commands.CmdPointAdd;
import commands.CmdPointUpdate;
import commands.CmdRectangleAdd;
import commands.CmdRectangleUpdate;
import commands.CmdSelect;
import commands.CmdToBack;
import commands.CmdToFront;
import commands.CmdUnselect;
import delete.CmdCircleDelete;
import delete.CmdLineDelete;
import delete.CmdPointDelete;
import dialogue.DlgParse;
import exceptions.IllegalRadiusException;
import modelViewController.ApplicationController;
import modelViewController.ApplicationFrame;
import modelViewController.ApplicationModel;
import shapes.Circle;
import shapes.Donut;
import shapes.Hexagon;
import shapes.Line;
import shapes.Point;
import shapes.Shape;
import shapes.Rectangle;
public class SerializeLog implements OptionChooser{

	private BufferedWriter writer;
	private BufferedReader reader;
	private ApplicationFrame AppFrame;
	private ApplicationModel AppModel;
	private ApplicationController AppController;
	private DlgParse dlgParse;
	
	private Point latestPoint;
	private Line latestLine;
	private Circle latestCircle;
	
	
	public SerializeLog(ApplicationFrame appFrame, ApplicationModel appModel, ApplicationController appController) {
		AppFrame = appFrame;
		AppModel = appModel;
		AppController = appController;
	}

	@Override
	public void saveFile(File file) {
		try {
			writer = new BufferedWriter(new FileWriter(file + ".log"));
			DefaultListModel<String> list = AppFrame.getDlmList();
			for (int i = 0; i < AppFrame.getDlmList().size(); i++) {
				writer.write(list.getElementAt(i));
				writer.newLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		try {
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void openFile(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
			dlgParse = new DlgParse();
			dlgParse.setFileLog(this);
			dlgParse.addCommand(reader.readLine());
			dlgParse.setVisible(true); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}
	
	public void readLogLine(String command) {
		try {
			String[] commands = command.split("->");
			switch(commands[0]) {
				case "Undo":
					AppController.undo();
					break;
				case "Redo":
					AppController.redo();
					break;
				case "Added":
					Shape shape = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					if(commands[1].split(":")[0].equals("Donut")) {
						AppController.executeCommand(new CmdDonutAdd(AppModel,(Donut)shape));				
					}
					else if(commands[1].split(":")[0].equals("Circle")) {
						latestCircle = (Circle) shape;
						AppController.executeCommand(new CmdCircleAdd(AppModel,latestCircle));
					}
					else if(commands[1].split(":")[0].equals("Hexagon")) {
						AppController.executeCommand(new CmdHexagonAdd(AppModel,(HexagonAdapter)shape));
					}
					else if(commands[1].split(":")[0].equals("Line")) {
						latestLine = (Line) shape;
						AppController.executeCommand(new CmdLineAdd(AppModel,latestLine));
					}
					else if(commands[1].split(":")[0].equals("Point")) {
						latestPoint = (Point) shape;
						AppController.executeCommand(new CmdPointAdd(AppModel,latestPoint));
					}
					else if(commands[1].split(":")[0].equals("Rectangle")) {
						AppController.executeCommand(new CmdRectangleAdd(AppModel,(Rectangle)shape));
					}
					AppFrame.getDlmList().addElement("Added->" + shape.toString());
					break;
				case "Updated":
					Shape oldShape = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					int index = AppModel.getIndexOfShape(oldShape);
					if (oldShape instanceof Point) {
						Point newPoint = parsePoint(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdPointUpdate((Point) AppModel.getByIndex(index), newPoint));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newPoint.toString());
					}
					else if (oldShape instanceof Line) {
						Line newLine = parseLine(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdLineUpdate((Line) AppModel.getByIndex(index), newLine));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newLine.toString());
					}
					else if (oldShape instanceof Rectangle) {
						Rectangle newRectangle = parseRectangle(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdRectangleUpdate((Rectangle) AppModel.getByIndex(index), newRectangle));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newRectangle.toString());
					}
					else if (oldShape instanceof Donut) {
						Donut newDonut = parseDonut(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdDonutUpdate((Donut) AppModel.getByIndex(index), newDonut));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newDonut.toString());
					}
					else if (oldShape instanceof Circle) {
						Circle newCircle = parseCircle(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdCircleUpdate((Circle) AppModel.getByIndex(index), newCircle));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newCircle.toString());
					}
					else if (oldShape instanceof HexagonAdapter) {
						HexagonAdapter newHexagon = parseHexagon(commands[2].split(":")[1]);
						AppController.executeCommand(new CmdHexagonUpdate((HexagonAdapter) AppModel.getByIndex(index), newHexagon));
						AppFrame.getDlmList().addElement("Updated->" + oldShape.toString() + "->" + newHexagon.toString());
					}
					break;
				case "Deleted":
					AppController.deleteFromLog();  
					break;
				case "Selected":
					Shape selectedShape = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.selectShapeFromLog(selectedShape);
					AppFrame.getDlmList().addElement("Selected->" + selectedShape.toString());
					break;
				case "Moved to front":
					Shape shapeMovedToFront = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.executeCommand(new CmdToFront(AppModel,AppModel.getShapes().indexOf(shapeMovedToFront), shapeMovedToFront));
					AppFrame.getDlmList().addElement("Moved to front->" + shapeMovedToFront.toString());
					break;
				case "Moved to back": 
					Shape shapeMovedToBack = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.executeCommand(new CmdToBack(AppModel, AppModel.getShapes().indexOf(shapeMovedToBack),shapeMovedToBack));
					AppFrame.getDlmList().addElement("Moved to back->" + shapeMovedToBack.toString());
					break;
				case "Brought to front":
					Shape shapeBroughtToFront = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.executeCommand(new CmdBringToFront(AppModel, shapeBroughtToFront));
					AppFrame.getDlmList().addElement("Brought to front->" + shapeBroughtToFront.toString());
					break;
				case "Brought to back":
					Shape shapeBroughtToBack = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.executeCommand(new CmdBringToBack(AppModel,AppModel.getShapes().indexOf(shapeBroughtToBack), shapeBroughtToBack));
					AppFrame.getDlmList().addElement("Brought to back->" + shapeBroughtToBack.toString());
					break;
				case "Unselected":
					Shape unselectedShape = parseShape(commands[1].split(":")[0], commands[1].split(":")[1]);
					AppController.executeCommand(new CmdUnselect(AppModel, unselectedShape,2));
					AppFrame.getDlmList().addElement("Unselected->" + unselectedShape.toString());
					break;
			}
			String line = reader.readLine();
			if (line != null) dlgParse.addCommand(line);
			else {
				dlgParse.closeDialog();
				return;
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private Shape parseShape(String shape, String shapeParameters) throws IllegalRadiusException {
		if (shape.equals("Point")) return parsePoint(shapeParameters);
		else if (shape.equals("Hexagon")) return parseHexagon(shapeParameters);
		else if (shape.equals("Line")) return parseLine(shapeParameters);
		else if (shape.equals("Circle")) return parseCircle(shapeParameters);
		else if (shape.equals("Rectangle")) return parseRectangle(shapeParameters);
		else if (shape.equals("Donut")) return parseDonut(shapeParameters);
		else return parseDonut(shapeParameters);
	}
	
	private Point parsePoint(String string) {
		String [] pointParts = string.split(";"); 		
		String s = pointParts[2].split("=")[1].substring(1, pointParts[2].split("=")[1].length() - 1);
		String [] colors = s.split(",");
		return new Point(Integer.parseInt(pointParts[0].split("=")[1]), Integer.parseInt(pointParts[1].split("=")[1]), new Color(Integer.parseInt(colors[0].split("-")[1]), Integer.parseInt(colors[1].split("-")[1]), Integer.parseInt(colors[2].split("-")[1])));
	}
	
	private Circle parseCircle(String string) throws NumberFormatException, IllegalRadiusException {
		String [] circleParts = string.split(";"); 	
		int radius = Integer.parseInt(circleParts[0].split("=")[1]);
		int x = Integer.parseInt(circleParts[1].split("=")[1]);
		int y = Integer.parseInt(circleParts[2].split("=")[1]);
		String s = circleParts[3].split("=")[1].substring(1, circleParts[3].split("=")[1].length() - 1);
		String [] edgeColors = s.split(",");
		String s1 = circleParts[4].split("=")[1].substring(1, circleParts[4].split("=")[1].length() - 1);
		String [] interiorColors = s1.split(",");
		return new Circle(new Point(x, y), radius, new Color(Integer.parseInt(edgeColors[0].split("-")[1]), Integer.parseInt(edgeColors[1].split("-")[1]), Integer.parseInt(edgeColors[2].split("-")[1])), new Color(Integer.parseInt(interiorColors[0].split("-")[1]), Integer.parseInt(interiorColors[1].split("-")[1]), Integer.parseInt(interiorColors[2].split("-")[1])));
	}
	
	private Line parseLine(String string) {
		String [] lineParts = string.split(";"); 	
		int xStart = Integer.parseInt(lineParts[0].split("=")[1]);
		int yStart = Integer.parseInt(lineParts[1].split("=")[1]);
		int xEnd = Integer.parseInt(lineParts[2].split("=")[1]);
		int yEnd = Integer.parseInt(lineParts[3].split("=")[1]);
		String s = lineParts[4].split("=")[1].substring(1, lineParts[4].split("=")[1].length() - 1);
		String [] edgeColors = s.split(",");
		Point startPoint = new Point(xStart, yStart);
		Point endPoint = new Point(xEnd, yEnd);
		Color lineColor = new Color(Integer.parseInt(edgeColors[0].split("-")[1]), Integer.parseInt(edgeColors[1].split("-")[1]), Integer.parseInt(edgeColors[2].split("-")[1]));
		return new Line(startPoint, endPoint, lineColor);
	}
	
	private HexagonAdapter parseHexagon(String string) throws IllegalRadiusException {
		String [] hexagonParts = string.split(";"); 	
		int radius = Integer.parseInt(hexagonParts[0].split("=")[1]);
		int x = Integer.parseInt(hexagonParts[1].split("=")[1]);
		int y = Integer.parseInt(hexagonParts[2].split("=")[1]);
		String s = hexagonParts[3].split("=")[1].substring(1, hexagonParts[3].split("=")[1].length() - 1);
		String [] edgeColors = s.split(",");
		String s1 = hexagonParts[4].split("=")[1].substring(1, hexagonParts[4].split("=")[1].length() - 1);
		String [] interiorColors = s1.split(",");
		Hexagon h = new Hexagon(x, y, radius);
		h.setBorderColor(new Color(Integer.parseInt(edgeColors[0].split("-")[1]), Integer.parseInt(edgeColors[1].split("-")[1]), Integer.parseInt(edgeColors[2].split("-")[1])));
		h.setAreaColor(new Color(Integer.parseInt(interiorColors[0].split("-")[1]), Integer.parseInt(interiorColors[1].split("-")[1]), Integer.parseInt(interiorColors[2].split("-")[1])));
		return new HexagonAdapter(h);
	}
	
	private Donut parseDonut(String string) throws NumberFormatException, IllegalRadiusException {
		String [] donutParts = string.split(";"); 	
		int radius = Integer.parseInt(donutParts[0].split("=")[1]);
		int x = Integer.parseInt(donutParts[1].split("=")[1]);
		int y = Integer.parseInt(donutParts[2].split("=")[1]);
		String s = donutParts[3].split("=")[1].substring(1, donutParts[3].split("=")[1].length() - 1);
		String [] edgeColors = s.split(",");
		String s1 = donutParts[4].split("=")[1].substring(1, donutParts[4].split("=")[1].length() - 1);
		String [] interiorColors = s1.split(",");
		int innerRadius = Integer.parseInt(donutParts[5].split("=")[1]);
		return new Donut(new Point(x, y), radius, innerRadius, new Color(Integer.parseInt(edgeColors[0].split("-")[1]), Integer.parseInt(edgeColors[1].split("-")[1]), Integer.parseInt(edgeColors[2].split("-")[1])), new Color(Integer.parseInt(interiorColors[0].split("-")[1]), Integer.parseInt(interiorColors[1].split("-")[1]), Integer.parseInt(interiorColors[2].split("-")[1])));
	}
	
	private Rectangle parseRectangle(String string) {
		String [] rectangleParts = string.split(";"); 	
		int x = Integer.parseInt(rectangleParts[0].split("=")[1]);
		int y = Integer.parseInt(rectangleParts[1].split("=")[1]);
		int height = Integer.parseInt(rectangleParts[2].split("=")[1]);
		int width = Integer.parseInt(rectangleParts[3].split("=")[1]);
		String s = rectangleParts[4].split("=")[1].substring(1, rectangleParts[4].split("=")[1].length() - 1);
		String [] edgeColors = s.split(",");
		String s1 = rectangleParts[5].split("=")[1].substring(1, rectangleParts[5].split("=")[1].length() - 1);
		String [] interiorColors = s1.split(",");
		return new Rectangle(new Point(x, y), width, height, new Color(Integer.parseInt(edgeColors[0].split("-")[1]), Integer.parseInt(edgeColors[1].split("-")[1]), Integer.parseInt(edgeColors[2].split("-")[1])), new Color(Integer.parseInt(interiorColors[0].split("-")[1]), Integer.parseInt(interiorColors[1].split("-")[1]), Integer.parseInt(interiorColors[2].split("-")[1])));
	}
}
