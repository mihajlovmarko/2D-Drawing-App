package modelViewController;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import delete.CmdDeleteShape;
import delete.CmdDonutDelete;
import delete.CmdHexagonDelete;
import delete.CmdLineDelete;
import delete.CmdPointDelete;
import delete.CmdRectangleDelete;
import deserialize.UnserializeImageFile;
import deserialize.UnserializeManager;
import dialogue.DlgCircle;
import dialogue.DlgCircleUpdate;
import dialogue.DlgDonut;
import dialogue.DlgDonutUpdate;
import dialogue.DlgHexagonUpdate;
import dialogue.DlgLine;
import dialogue.DlgPoint;
import dialogue.DlgRectangle;
import dialogue.DlgRectangleUpdate;
import exceptions.IllegalRadiusException;
import shapes.Circle;
import shapes.Command;
import shapes.Donut;
import shapes.Hexagon;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import strategy.FileManager;
import strategy.SerializeDrawing;
import strategy.SerializeFile;
import strategy.SerializeLog;
import dialogue.DlgSigurni;

public class ApplicationController implements PropertyChangeListener{
	private ApplicationModel AppModel;
	private ApplicationFrame AppFrame;

	private Color outColor = Color.BLACK;
	private Color inColor = Color.WHITE;
	private DefaultListModel<String> actLog;
	private FileManager fileManager;
	private int selSize = 0;
	
	
	public ApplicationController(ApplicationModel AppModel, ApplicationFrame AppFrame)
	{
		this.AppModel = AppModel;
		this.AppFrame = AppFrame;
		this.actLog = AppFrame.getDlmList();
	}

	public void mouseClicked(MouseEvent e) throws IllegalRadiusException {
		stateChecker(e);
		if(checkShapes(e) == false) {
			unselectAll();
			AppFrame.getAppView().repaint();
		}
		AppFrame.getAppView().repaint();
	}
	private PropertyChangeEvent pce;
	@Override 
	public void propertyChange(PropertyChangeEvent evt) {
		this.pce = evt;
		//		if((int) evt.getNewValue()==1 && evt.getPropertyName() == "Selected Shapes" || evt.getPropertyName() == "Deleted Shapes") {
		//			AppFrame.getTglBtnModify().setVisible(true);
		//			AppFrame.getTglBtnDelete().setVisible(true);
		//		} else {
		//			if(AppModel.getSelectedShapes().size()==1) {
		//				AppFrame.getTglBtnModify().setVisible(true);
		//				AppFrame.getTglBtnDelete().setVisible(true);
		//			} else {
		//				System.out.println("test");
		//				AppFrame.getTglBtnModify().setVisible(false);
		//				AppFrame.getTglBtnDelete().setVisible(false);
		//			}
		//		
		//		}
		if((int) evt.getNewValue() == 1 && evt.getPropertyName() == "Selected Shapes" || AppModel.getSelectedShapes().size() == 1) {
			AppFrame.getTglBtnModify().setVisible(true);
//			AppFrame.getTglBtnDelete().setVisible(true);
		} 
		else {
			AppFrame.getTglBtnModify().setVisible(false);
//			AppFrame.getTglBtnDelete().setVisible(false);
		}
		
		if((int) evt.getNewValue() == 1 && evt.getPropertyName() == "Selected Shapes" || AppModel.getSelectedShapes().size() > 0) {
			AppFrame.getTglBtnDelete().setVisible(true);
		} else {
			AppFrame.getTglBtnDelete().setVisible(false);
		}

		if((int) evt.getNewValue() == 0 && evt.getPropertyName() == "Deleted Shapes") {
			AppFrame.getTglBtnModify().setVisible(false);
			AppFrame.getTglBtnDelete().setVisible(false);
		}

		if(evt.getPropertyName() == "Undo Stack" && (int) evt.getNewValue() > 0) {
			AppFrame.getBtnUndo().setVisible(true);
		} else if ((int)evt.getNewValue() == 0 && evt.getPropertyName() == "Undo Stack Remove"){
			AppFrame.getBtnUndo().setVisible(false);
		}

		if(evt.getPropertyName() == "Redo Stack" && (int) evt.getNewValue() > 0) {
			AppFrame.getBtnRedo().setVisible(true);
		} else if ((int) evt.getNewValue() == 0 && evt.getPropertyName() == "Redo Stack Remove") {
			AppFrame.getBtnRedo().setVisible(false);
		}

	}


	private void drawPoint(MouseEvent e) {
		Point point = new Point(e.getX(), e.getY(), getOutColor());
		CmdPointAdd CmdPointAdd = new CmdPointAdd(AppModel, point);
		CmdPointAdd.execute();
		AppModel.pushToUndoStack(CmdPointAdd);
		actLog.addElement("Added->" + point.toString());
		AppFrame.getBtnRedo().setVisible(false);
		AppModel.getRedoStack().removeAllElements();

	}
	private void drawLine(MouseEvent e){
		if(AppModel.getStartPoint() == null)
			AppModel.setStartPoint(new Point(e.getX(), e.getY()));
		else
		{
			Line line = new Line(AppModel.getStartPoint(), new Point(e.getX(), e.getY()), outColor);
			CmdLineAdd CmdLineAdd = new CmdLineAdd(AppModel, line);
			CmdLineAdd.execute();
			AppModel.pushToUndoStack(CmdLineAdd);
			AppModel.setStartPoint(null);
			actLog.addElement("Added->" + line.toString());
			AppFrame.getBtnRedo().setVisible(false);
			AppModel.getRedoStack().removeAllElements();
		}
	}

	private void drawCircle(MouseEvent e) {
		DlgCircle dlgCircle = new DlgCircle();
		dlgCircle.setVisible(true);

		if(dlgCircle.isConfirm()) {
			try {
				if(checkType(dlgCircle.getTxtRadius().getText())) {
					int radius = Integer.parseInt(dlgCircle.getTxtRadius().getText());
					Circle circle = new Circle(new Point(e.getX(), e.getY()), radius, outColor, inColor);
					CmdCircleAdd CmdCircleAdd = new CmdCircleAdd(AppModel,circle);
					CmdCircleAdd.execute();
					AppModel.pushToUndoStack(CmdCircleAdd);
					actLog.addElement("Added->" + circle.toString());
					AppFrame.getBtnRedo().setVisible(false);
					AppModel.getRedoStack().removeAllElements();
				} else {
					JOptionPane.showMessageDialog(AppFrame,
							"Illegal input type!",
							"Illegal radius error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IllegalRadiusException e1) {
				JOptionPane.showMessageDialog(AppFrame,
						"Radius must be greater than 0!",
						"Illegal radius error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void drawRectangle(MouseEvent e) {
		DlgRectangle dlgRectangle = new DlgRectangle();
		dlgRectangle.setVisible(true);

		if(dlgRectangle.isConfirm()) {
			if(checkType(dlgRectangle.getTxtWidth().getText()) && checkType(dlgRectangle.getTxtHeight().getText())) {
				Rectangle rectangle = new Rectangle(new Point(e.getX(), e.getY()),Integer.parseInt(dlgRectangle.getTxtWidth().getText()),Integer.parseInt(dlgRectangle.getTxtHeight().getText()), outColor, inColor);
				CmdRectangleAdd CmdRectangleAdd = new CmdRectangleAdd(AppModel, rectangle);
				CmdRectangleAdd.execute();
				AppModel.pushToUndoStack(CmdRectangleAdd);
				actLog.addElement("Added->" + rectangle.toString());
				AppFrame.getBtnRedo().setVisible(false);
				AppModel.getRedoStack().removeAllElements();
			} else {
				JOptionPane.showMessageDialog(AppFrame,
						"Illegal input type!",
						"Illegal radius error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private void drawDonut(MouseEvent e) {
		DlgDonut dlgDonut = new DlgDonut();
		dlgDonut.setVisible(true);

		if(dlgDonut.isConfirm()) {
			try {
				if(checkType(dlgDonut.getTxtOuterRadius().getText())) {
					if(dlgDonut.getTxtOuterRadius().getText() != null && dlgDonut.getTxtInnerRadius().getText() != null ) {
						Donut donut = new Donut(new Point(e.getX(), e.getY()), Integer.parseInt(dlgDonut.getTxtOuterRadius().getText()), Integer.parseInt(dlgDonut.getTxtInnerRadius().getText()), outColor, inColor);
						CmdDonutAdd CmdDonutAdd = new CmdDonutAdd(AppModel,donut);
						CmdDonutAdd.execute();
						AppModel.pushToUndoStack(CmdDonutAdd);
						actLog.addElement("Added->" + donut.toString());
						AppFrame.getBtnRedo().setVisible(false);
						AppModel.getRedoStack().removeAllElements();
					}
				} else {
					JOptionPane.showMessageDialog(AppFrame,
							"Illegal input type!",
							"Illegal radius error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IllegalRadiusException e1) {
				JOptionPane.showMessageDialog(AppFrame,
						"Inner radius must be smaller than outer radius!",
						"Illegal radius error",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	} 
	private void drawHexagon(MouseEvent e) { 
		DlgCircle dlgHex = new DlgCircle();
		dlgHex.setTitle("Add Hexagon");
		dlgHex.setVisible(true);

		if(dlgHex.isConfirm()) {
			try {
				if(checkType(dlgHex.getTxtRadius().getText())) {
					Hexagon hexagon = new Hexagon(e.getX(),e.getY(),Integer.parseInt(dlgHex.getTxtRadius().getText()), outColor, inColor);
					HexagonAdapter adapter = new HexagonAdapter(hexagon);
					CmdHexagonAdd CmdHexagonAdd = new CmdHexagonAdd(AppModel,adapter);
					CmdHexagonAdd.execute();
					AppModel.pushToUndoStack(CmdHexagonAdd);
					actLog.addElement("Added->" + adapter.toString());
					AppFrame.getBtnRedo().setVisible(false);
					AppModel.getRedoStack().removeAllElements();
				} else {
					JOptionPane.showMessageDialog(AppFrame,
							"Illegal input type!",
							"Illegal radius error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (IllegalRadiusException e1) {
				JOptionPane.showMessageDialog(AppFrame,
						"Radius must be greater than 0!",
						"Illegal radius error",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
	}

	public void undo() {
		if(AppModel.getUndoStack().size()>0) {
			Command command = AppModel.getUndoStack().peek();
			AppModel.pushToRedoStack(command);
			actLog.addElement("Undo->" + AppModel.getUndoStack().peek().toString());
			AppModel.removeFromUndoStack();
			AppFrame.getAppView().repaint(); 
		}
		System.out.println(AppModel.getSelectedShapes());
	}

	public void redo() {
		if(AppModel.getRedoStack().size()>0) {
//			if(AppModel.getRedoStack().peek() instanceof CmdUnselect) {
//				AppModel.pushToUndoStack(AppModel.getRedoStack().peek());
//				actLog.addElement("Redo->" + AppModel.getRedoStack().peek().toString());
//				AppModel.removeFromRedoStack();
//				AppFrame.getAppView().repaint();
//				System.out.println(AppModel.getSelectedShapes());
//				return;
//			}
			AppModel.pushToUndoStack(AppModel.getRedoStack().peek());
			actLog.addElement("Redo->" + AppModel.getRedoStack().peek().toString());
			AppModel.removeFromRedoStack();
			AppFrame.getAppView().repaint();
		}
		System.out.println(AppModel.getSelectedShapes());
	}

	private void selectShape(MouseEvent e) {
		for(int i = 0; i<AppModel.getShapes().size(); i++)
		{
			if(AppModel.getShapes().get(i).contains(e.getX(), e.getY()))
			{
				if(AppModel.getShapes().get(i).isSelected())
				{
					return;
				}
				Shape shape = AppModel.getShapes().get(i);
				CmdSelect CmdSelect = new CmdSelect(AppModel, shape);
				CmdSelect.execute();
				actLog.addElement("Selected->" + shape.toString());
				AppModel.getUndoStack().push(CmdSelect);
				selSize++;
			}
		}
		
//		for(int x = 0; x<AppModel.getShapes().size(); x++) {
//			for(int y = x + 1; y<AppModel.getShapes().size(); y++ ) {
//				if(AppModel.getShapes().get(x).contains(e.getX(), e.getY()) && AppModel.getShapes().get(y).contains(e.getX(), e.getY())) {
//					AppModel.removeSelectedShape(AppModel.getShapes().get(x));
//					Shape shape = AppModel.getShapes().get(y);
//					CmdSelect CmdSelect = new CmdSelect(AppModel, shape);
//					CmdSelect.execute();
//					actLog.addElement("Selected->" + shape.toString());
//					AppModel.getUndoStack().push(CmdSelect);
//				}
//			}
//		}
//		if(AppModel.getShapes().size()>1)
//		{
//			for(int i = 0; i<AppModel.getShapes().size(); i++)
//			{
//				if(AppModel.getShapes().get(i).contains(e.getX(), e.getY()) && AppModel.getShapes().get(AppModel.getShapes().size()-1).contains(e.getX(), e.getY()))
//				{
//					AppModel.getShapes().get(i).setSelected(false);
//					AppModel.getShapes().get(AppModel.getShapes().size()-1).setSelected(true);
//				}
//			}
//		}
	} 
	
	public void selectShapeFromLog(Shape shape) {
		int index = AppModel.getShapes().indexOf(shape);
		Shape selectedShape = AppModel.getShapes().get(index);
		CmdSelect CmdSelect = new CmdSelect(AppModel, selectedShape);
		CmdSelect.execute();
		AppModel.getUndoStack().push(CmdSelect);
		AppFrame.getAppView().repaint();
	}

	public void unselectAll() {

//		for(int i = 0; i< AppModel.getSelectedShapes().size(); i++) {
//				Shape shape = AppModel.getSelectedShapes().get(i);
//				CmdUnselect unselect = new CmdUnselect(AppModel, shape,0);
//				unselect.execute();
//				AppModel.getUndoStack().push(unselect);
//				actLog.addElement("Unselected->" + shape.toString());
//		}
//		AppModel.getSelectedShapes().clear();
//		Iterator<Shape> it = AppModel.getSelectedShapes().iterator();
//		while(it.hasNext()) {
//		Shape shape = it.next();
//		actLog.addElement("Unselected->" + shape.toString());
//			it.next().setSelected(false);
//			it.remove();
//		}
//		
		for(int i = 0; i<AppModel.getShapes().size(); i++) {
			if(AppModel.getShapes().get(i).isSelected()) {
				AppModel.getShapes().get(i).setSelected(false);
			}
		}
		AppModel.getSelectedShapes().clear();
		System.out.println(AppModel.getSelectedShapes());
		AppFrame.getTglBtnModify().setVisible(false);
		AppFrame.getTglBtnDelete().setVisible(false);
	}

	public void modifyShape() throws IllegalRadiusException
	{
		if(AppModel.getSelectedShapes().get(0) instanceof Point) {
			if(AppModel.getSelectedShapes().get(0).isSelected()) {
				DlgPoint dlgPoint = new DlgPoint();
				Point oldState = (Point) AppModel.getSelectedShapes().get(0);
				dlgPoint.getTxtX().setText(Integer.toString(oldState.getX()));
				dlgPoint.getTxtY().setText(Integer.toString(oldState.getY()));
				dlgPoint.setVisible(true);
				if(dlgPoint.isConfirm()) {
					if(checkType(dlgPoint.getTxtX().getText()) && checkType(dlgPoint.getTxtY().getText())) {
						Point newState = new Point(Integer.parseInt(dlgPoint.getTxtX().getText()), Integer.parseInt(dlgPoint.getTxtY().getText()), dlgPoint.getColor());
						actLog.addElement("Updated->" + oldState.toString() + "->" + newState.toString());
						CmdPointUpdate CmdPointUpdate = new CmdPointUpdate(oldState , newState);
						CmdPointUpdate.execute();
						AppModel.pushToUndoStack(CmdPointUpdate);
					//	AppModel.getRedoStack().removeAllElements();
						AppFrame.repaint();
					} else {
						JOptionPane.showMessageDialog(AppFrame,
								"Illegal input type!",
								"Illegal radius error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (AppModel.getSelectedShapes().get(0) instanceof Line) {

			if(AppModel.getSelectedShapes().get(0).isSelected()) {
				DlgLine dlgLine = new DlgLine();
				Line oldLine = (Line) AppModel.getSelectedShapes().get(0);
				dlgLine.getTxtStartPointX().setText((Integer.toString(oldLine.getStartPoint().getX())));
				dlgLine.getTxtStartPointY().setText((Integer.toString(oldLine.getStartPoint().getY())));
				dlgLine.getTxtEndPointX().setText((Integer.toString(oldLine.getEndPoint().getX())));
				dlgLine.getTxtEndPointY().setText((Integer.toString(oldLine.getEndPoint().getY())));
				dlgLine.setVisible(true);
				if(dlgLine.isConfirmation()) {
					if(checkType(dlgLine.getTxtStartPointX().getText()) && checkType(dlgLine.getTxtStartPointY().getText()) && checkType(dlgLine.getTxtEndPointX().getText()) && checkType(dlgLine.getTxtEndPointY().getText())) {
						Line newLine = new Line(new Point(Integer.parseInt(dlgLine.getTxtStartPointX().getText()), Integer.parseInt(dlgLine.getTxtStartPointY().getText())), new Point(Integer.parseInt(dlgLine.getTxtEndPointX().getText()), Integer.parseInt(dlgLine.getTxtEndPointY().getText())), dlgLine.getColor());
						CmdLineUpdate CmdLineUpdate = new CmdLineUpdate(oldLine,newLine);
						actLog.addElement("Updated->" + oldLine.toString() + "->" + newLine.toString());
						CmdLineUpdate.execute();
						AppModel.pushToUndoStack(CmdLineUpdate);
				//		AppModel.getRedoStack().removeAllElements();
						AppFrame.repaint();
					} else {
						JOptionPane.showMessageDialog(AppFrame,
								"Illegal input type!",
								"Illegal radius error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (AppModel.getSelectedShapes().get(0) instanceof Rectangle) {
			if(AppModel.getSelectedShapes().get(0).isSelected()) {
				DlgRectangleUpdate dlgRectangleUpdate = new DlgRectangleUpdate();
				Rectangle oldRectangle = (Rectangle) AppModel.getSelectedShapes().get(0);
				dlgRectangleUpdate.getTxtUpperLeftPointX().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getX()));
				dlgRectangleUpdate.getTxtUpperLeftPointY().setText(Integer.toString(oldRectangle.getUpperLeftPoint().getY()));
				dlgRectangleUpdate.getTxtHeight().setText(Integer.toString(oldRectangle.getHeight()));
				dlgRectangleUpdate.getTxtWidth().setText(Integer.toString(oldRectangle.getWidth()));
				dlgRectangleUpdate.setVisible(true);
				if(dlgRectangleUpdate.isConfirmation()) {
					if(checkType(dlgRectangleUpdate.getTxtUpperLeftPointX().getText()) && checkType(dlgRectangleUpdate.getTxtUpperLeftPointY().getText()) && checkType(dlgRectangleUpdate.getTxtWidth().getText()) && checkType(dlgRectangleUpdate.getTxtHeight().getText())) {
						Rectangle newRectangle = new Rectangle(new Point(Integer.parseInt(dlgRectangleUpdate.getTxtUpperLeftPointX().getText()), Integer.parseInt(dlgRectangleUpdate.getTxtUpperLeftPointY().getText())), Integer.parseInt(dlgRectangleUpdate.getTxtWidth().getText()), Integer.parseInt(dlgRectangleUpdate.getTxtHeight().getText()),dlgRectangleUpdate.getOutlineColor(), dlgRectangleUpdate.getFillColor());
						CmdRectangleUpdate CmdRectangleUpdate = new CmdRectangleUpdate(oldRectangle,newRectangle);
						actLog.addElement("Updated->" + oldRectangle.toString() + "->" + newRectangle.toString());
						CmdRectangleUpdate.execute();
						AppModel.pushToUndoStack(CmdRectangleUpdate);
						AppModel.getRedoStack().removeAllElements();
						AppFrame.repaint();
					} else {
						JOptionPane.showMessageDialog(AppFrame,
								"Illegal input type!",
								"Illegal radius error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if (AppModel.getSelectedShapes().get(0) instanceof Donut) {
			if(AppModel.getSelectedShapes().get(0).isSelected()) {
				DlgDonutUpdate dlgDonutUpdate = new DlgDonutUpdate();
				Donut oldDonut = (Donut) AppModel.getSelectedShapes().get(0);
				dlgDonutUpdate.getTxtX().setText(Integer.toString(oldDonut.getCenter().getX()));
				dlgDonutUpdate.getTxtY().setText(Integer.toString(oldDonut.getCenter().getY()));
				dlgDonutUpdate.getTxtInnerRadius().setText(Integer.toString(oldDonut.getInnerRadius()));
				dlgDonutUpdate.getTxtOuterRadius().setText(Integer.toString(oldDonut.getOuterRadius()));
				dlgDonutUpdate.setVisible(true);
				if(dlgDonutUpdate.isConfirm()) {
					if(checkType(dlgDonutUpdate.getTxtX().getText()) && checkType(dlgDonutUpdate.getTxtY().getText()) && checkType(dlgDonutUpdate.getTxtOuterRadius().getText()) && checkType(dlgDonutUpdate.getTxtInnerRadius().getText())) {
						Donut newDonut = new Donut(new Point(Integer.parseInt(dlgDonutUpdate.getTxtX().getText()), Integer.parseInt(dlgDonutUpdate.getTxtY().getText())), Integer.parseInt(dlgDonutUpdate.getTxtOuterRadius().getText()), Integer.parseInt(dlgDonutUpdate.getTxtInnerRadius().getText()), dlgDonutUpdate.getBorderColor(), dlgDonutUpdate.getFillColor());
						CmdDonutUpdate CmdDonutUpdate = new CmdDonutUpdate(oldDonut, newDonut);
						actLog.addElement("Updated->" + oldDonut.toString() + "->" + newDonut.toString());
						CmdDonutUpdate.execute();
						AppModel.pushToUndoStack(CmdDonutUpdate);
						AppModel.getRedoStack().removeAllElements();
						AppFrame.repaint();
					} else {
						JOptionPane.showMessageDialog(AppFrame,
								"Illegal input type!",
								"Illegal radius error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if (AppModel.getSelectedShapes().get(0) instanceof Circle) { 
			/*
			 * Novi dijalog za svaki oblik osim point i line zbog boje
			 */
			if(AppModel.getSelectedShapes().get(0).isSelected()) {
				DlgCircleUpdate dlgCircleUpdate = new DlgCircleUpdate();
				Circle oldCircle = (Circle) AppModel.getSelectedShapes().get(0);
				dlgCircleUpdate.getTxtRadius().setText(Integer.toString(oldCircle.getR()));
				dlgCircleUpdate.getTxtCenterX().setText(Integer.toString(oldCircle.getCenter().getX()));
				dlgCircleUpdate.getTxtCenterY().setText(Integer.toString(oldCircle.getCenter().getY()));
				dlgCircleUpdate.setVisible(true);
				if(dlgCircleUpdate.isConfirmation()) {
					try {
						if(checkType(dlgCircleUpdate.getTxtCenterX().getText()) && checkType(dlgCircleUpdate.getTxtCenterY().getText()) && checkType(dlgCircleUpdate.getTxtRadius().getText())) {
							Circle newCircle = new Circle(new Point(Integer.parseInt(dlgCircleUpdate.getTxtCenterX().getText()), Integer.parseInt(dlgCircleUpdate.getTxtCenterY().getText())), Integer.parseInt(dlgCircleUpdate.getTxtRadius().getText()),dlgCircleUpdate.getOutlineColor(), dlgCircleUpdate.getFillColor());
							CmdCircleUpdate CmdCircleUpdate = new CmdCircleUpdate(oldCircle, newCircle);
							actLog.addElement("Updated->" + oldCircle.toString() + "->" + newCircle.toString());
							CmdCircleUpdate.execute();
							AppModel.pushToUndoStack(CmdCircleUpdate);
							AppModel.getRedoStack().removeAllElements();
							AppFrame.repaint();
						} else {
							JOptionPane.showMessageDialog(AppFrame,
									"Illegal input type!",
									"Illegal radius error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (AppModel.getSelectedShapes().get(0) instanceof HexagonAdapter) {
			if(((HexagonAdapter) AppModel.getSelectedShapes().get(0)).getHexagon().isSelected()) {
				DlgHexagonUpdate dlgHexagonUpdate = new DlgHexagonUpdate();
				HexagonAdapter oldHexagon = (HexagonAdapter) AppModel.getSelectedShapes().get(0);
				dlgHexagonUpdate.getTxtCenterX().setText(Integer.toString(oldHexagon.getHexagon().getX()));
				dlgHexagonUpdate.getTxtCenterY().setText(Integer.toString(oldHexagon.getHexagon().getY()));
				dlgHexagonUpdate.getTxtR().setText(Integer.toString(oldHexagon.getHexagon().getR()));
				dlgHexagonUpdate.setVisible(true);
				if(dlgHexagonUpdate.isConfirmation()) {
					try {
						if(checkType(dlgHexagonUpdate.getTxtCenterX().getText()) && checkType(dlgHexagonUpdate.getTxtCenterY().getText()) && checkType(dlgHexagonUpdate.getTxtR().getText())) {
							Hexagon hex = new Hexagon(Integer.parseInt(dlgHexagonUpdate.getTxtCenterX().getText()), Integer.parseInt(dlgHexagonUpdate.getTxtCenterY().getText()), Integer.parseInt(dlgHexagonUpdate.getTxtR().getText()), dlgHexagonUpdate.getOutlineColor(), dlgHexagonUpdate.getFillColor());
							HexagonAdapter adapter = new HexagonAdapter(hex);
							CmdHexagonUpdate CmdHexagonUpdate = new CmdHexagonUpdate(oldHexagon, adapter);
							actLog.addElement("Updated->" + oldHexagon.toString() + "->" + adapter.toString());
							CmdHexagonUpdate.execute();
							AppModel.pushToUndoStack(CmdHexagonUpdate);
							AppModel.getRedoStack().removeAllElements();
							AppFrame.repaint();
						} else {
							JOptionPane.showMessageDialog(AppFrame,
									"Illegal input type!",
									"Illegal radius error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IllegalRadiusException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void toFront() {
		if(AppModel.getSelectedShapes().size() == 1) {
			int index = AppModel.getShapes().indexOf(AppModel.getSelectedShapes().get(0));
			Shape shape = AppModel.getShapes().get(index);
			CmdToFront ToFront = new CmdToFront(AppModel, index , shape);
			AppModel.pushToUndoStack(ToFront);
			ToFront.execute();
			actLog.addElement("Moved to front->" + shape.toString());
		} else {
			System.out.println("Selektovano vise od 2 oblika.");
		}
		AppFrame.repaint();
	}

	public void toBack() {
		if(AppModel.getSelectedShapes().size() == 1) {
			int index = AppModel.getShapes().indexOf(AppModel.getSelectedShapes().get(0));
			Shape shape = AppModel.getShapes().get(index);
			CmdToBack ToBack = new CmdToBack(AppModel, index, shape);
			AppModel.pushToUndoStack(ToBack);
			actLog.addElement("Moved to back->" + shape.toString());
			ToBack.execute();
		}
		AppFrame.repaint();
	}

	public void bringToFront() {
		if(AppModel.getSelectedShapes().size() == 1) {
			int index = AppModel.getShapes().indexOf(AppModel.getSelectedShapes().get(0));
			Shape shape = AppModel.getShapes().get(index);
			CmdBringToFront BringToFront = new CmdBringToFront(AppModel,shape);
			AppModel.pushToUndoStack(BringToFront);
			actLog.addElement("Brought to front->" + shape.toString());
			BringToFront.execute();
		}
		AppFrame.repaint();
	}

	public void bringToBack() {
		if(AppModel.getSelectedShapes().size() == 1) {
			int index = AppModel.getShapes().indexOf(AppModel.getSelectedShapes().get(0));
			Shape shape = AppModel.getShapes().get(index);
			CmdBringToBack BringToBack = new CmdBringToBack(AppModel, index, shape);
			AppModel.pushToUndoStack(BringToBack);
			actLog.addElement("Brought to back->" + shape.toString());
			BringToBack.execute();
		}
		AppFrame.repaint();
	}

	public void deleteFromLog() {
		while(AppModel.getSelectedShapes().size()>0) {
			for(int i = 0; i<AppModel.getSelectedShapes().size(); i++) {
				Shape shape = AppModel.getSelectedShapes().get(i);
				CmdDeleteShape CDS = new CmdDeleteShape(AppModel, shape);
				CDS.execute();
				actLog.addElement("Deleted->" + shape.toString());
				AppModel.getUndoStack().push(CDS);
		}
			AppFrame.getAppView().repaint();
	}
		
	}
	
	
	public void delete()
	{

		DlgSigurni dlgChoice = new DlgSigurni();
		dlgChoice.setVisible(true);
		if(dlgChoice.potvrda) {
			while(AppModel.getSelectedShapes().size()>0) {
				for(int i = 0; i<AppModel.getSelectedShapes().size(); i++) {
					Shape shape = AppModel.getSelectedShapes().get(i);
					CmdDeleteShape CDS = new CmdDeleteShape(AppModel, shape);
					CDS.execute();
					actLog.addElement("Deleted->" + shape.toString());
					AppModel.getUndoStack().push(CDS);
			}
//			if(dlgChoice.potvrda) {
//				for(int i = 0; i<AppModel.getSelectedShapes().size(); i++) {
//					Shape shape = AppModel.getSelectedShapes().get(i);
//					CmdDeleteShape CDS = new CmdDeleteShape(AppModel, shape);
//					CDS.execute();
//					actLog.addElement("Deleted->" + shape.toString());
//					AppModel.getUndoStack().push(CDS);

				}
		}
	
			//			for(int i = 0; i<AppModel.getSelectedShapes().size(); i++)
			//			{
			//				if(AppModel.getSelectedShapes().get(0) instanceof Point) {
			//					Point point = (Point) AppModel.getSelectedShapes().get(0);
			//					CmdPointDelete CmdPointDelete = new CmdPointDelete(AppModel, point);
			//					CmdPointDelete.execute();
			//					actLog.addElement("Deleted->" + point.toString());
			//					AppModel.getUndoStack().push(CmdPointDelete);
			//				} else if (AppModel.getSelectedShapes().get(0) instanceof Line) {
			//					Line line = (Line) AppModel.getSelectedShapes().get(0);
			//					CmdLineDelete CmdLineDelete = new CmdLineDelete(AppModel, line);
			//					CmdLineDelete.execute();
			//					actLog.addElement("Deleted->" + line.toString());
			//					AppModel.getUndoStack().push(CmdLineDelete);
			//				} else if (AppModel.getSelectedShapes().get(0) instanceof Rectangle) {
			//					Rectangle rectangle = (Rectangle) AppModel.getSelectedShapes().get(0);
			//					CmdRectangleDelete CmdRectangleDelete = new CmdRectangleDelete(AppModel, rectangle);
			//					CmdRectangleDelete.execute();
			//					actLog.addElement("Deleted->" + rectangle.toString());
			//					AppModel.getUndoStack().push(CmdRectangleDelete);
			//				} else if (AppModel.getSelectedShapes().get(0) instanceof Donut){
			//					Donut donut = (Donut) AppModel.getSelectedShapes().get(0);
			//					CmdDonutDelete CmdDonutDelete = new CmdDonutDelete(AppModel,donut);
			//					CmdDonutDelete.execute();
			//					actLog.addElement("Deleted->" + donut.toString());
			//					AppModel.getUndoStack().push(CmdDonutDelete);
			//				} else if (AppModel.getSelectedShapes().get(0) instanceof Circle) {
			//					Circle circle = (Circle) AppModel.getSelectedShapes().get(0);
			//					CmdCircleDelete CmdCircleDelete = new CmdCircleDelete(AppModel, circle);
			//					CmdCircleDelete.execute();
			//					actLog.addElement("Deleted->" + circle.toString());
			//					AppModel.getUndoStack().push(CmdCircleDelete);
			//				} else if (AppModel.getSelectedShapes().get(0) instanceof HexagonAdapter) {
			//					HexagonAdapter hexagon = (HexagonAdapter) AppModel.getSelectedShapes().get(0);
			//					CmdHexagonDelete CmdHexagonDelete = new CmdHexagonDelete(AppModel, hexagon);
			//					CmdHexagonDelete.execute();
			//					actLog.addElement("Deleted->" + hexagon.getHexagon().toString());
			//					AppModel.getUndoStack().push(CmdHexagonDelete);
			//				}
			//			}
			AppFrame.repaint();
		}
	
	private boolean checkType(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch(NumberFormatException e1) {
			return false;
		}
	}

	private boolean checkShapes(MouseEvent e) {
		for(int i = 0; i<AppModel.getShapes().size(); i++) {
			if(AppModel.getShapes().get(i).contains(e.getX(), e.getY())) {
				return true;
			}
		}
		return false;
	}
	
	
	public void serialize() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY); 
		chooser.enableInputMethods(false);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileHidingEnabled(false);
		chooser.setEnabled(true);
		chooser.setDialogTitle("Save");
		chooser.setAcceptAllFileFilterUsed(false);

		if (!AppModel.getShapes().isEmpty()) {
			chooser.setFileFilter(new FileNameExtensionFilter("Serialized draw", "ser"));
//			chooser.setFileFilter(new FileNameExtensionFilter("Image", "img"));
		}
		if (!AppModel.getUndoStack().isEmpty() || AppModel.getShapes().isEmpty()) chooser.setFileFilter(new FileNameExtensionFilter("Commands log", "log"));
		if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (chooser.getFileFilter().getDescription().equals("Serialized draw")) fileManager = new FileManager(new SerializeFile(AppModel));
			else if (chooser.getFileFilter().getDescription().equals("Commands log")) fileManager = new FileManager(new SerializeLog(AppFrame, AppModel, this));
			else /*if (chooser.getFileFilter().getDescription().equals("Image"))*/ fileManager = new FileManager(new SerializeDrawing(AppFrame));
			fileManager.saveFile(chooser.getSelectedFile());
		}
		chooser.setVisible(false);
	}


	public void unserialize() {
		JFileChooser chooser = new JFileChooser();
		chooser.enableInputMethods(true);
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileHidingEnabled(false);
		chooser.setEnabled(true);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
		chooser.setFileFilter(new FileNameExtensionFilter("Serialized draw", "ser"));
		chooser.setFileFilter(new FileNameExtensionFilter("Commands log", "log"));

		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			AppFrame.getBtnUndo().setVisible(false);
			AppFrame.getBtnRedo().setVisible(false);
			AppFrame.getDlmList().clear();
			AppModel.getShapes().clear();
			AppModel.getUndoStack().clear();
			AppModel.getRedoStack().clear();
			AppFrame.getAppView().repaint();
			if (chooser.getFileFilter().getDescription().equals("Serialized draw")) {
				fileManager = new FileManager(new SerializeFile(AppModel));
			}
			else if (chooser.getFileFilter().getDescription().equals("Commands log")) fileManager = new FileManager(new SerializeLog(AppFrame, AppModel, this));
			fileManager.openFile(chooser.getSelectedFile());
		}	
		chooser.setVisible(false);
	}

	//Da li ovde treba da se brise redo stack
	
	public void executeCommand(Command command) {
		command.execute();
		AppModel.pushToUndoStack(command);
		AppFrame.getAppView().repaint();
	}

	public Color getOutColor() {
		return outColor;
	}

	public void setOutColor(Color outColor) {
		this.outColor = outColor;
	}

	public Color getInColor() {
		return inColor;
	}

	public void setInColor(Color inColor) {
		this.inColor = inColor;
	}

	private void stateChecker(MouseEvent e) throws IllegalRadiusException {
		if(AppFrame.getState() == 1)
		{
			drawPoint(e);
		}
		else if (AppFrame.getState() == 2)
		{
			drawLine(e);
		}
		else if (AppFrame.getState() == 3)
		{
			drawCircle(e);
		}
		else if (AppFrame.getState() == 4)
		{
			drawRectangle(e);
		}
		else if(AppFrame.getState() == 5)
		{
			drawDonut(e);
		}
		else if(AppFrame.getState() == 6)
		{
			drawHexagon(e);
		}
		else if(AppFrame.getState() == 7)           
		{
			selectShape(e);
		}
		else if(AppFrame.getState() == 8)
		{
			modifyShape();
		}
	}
}


