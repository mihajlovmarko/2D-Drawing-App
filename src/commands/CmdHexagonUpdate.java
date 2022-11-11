package commands;


import java.awt.Color;

import adapters.HexagonAdapter;
import exceptions.IllegalRadiusException;
import shapes.Command;
import shapes.Hexagon;

public class CmdHexagonUpdate implements Command{
	private HexagonAdapter oldState;
	private HexagonAdapter newState;
	private HexagonAdapter originalState = new HexagonAdapter(new Hexagon());
	
	public CmdHexagonUpdate( HexagonAdapter oldState, HexagonAdapter newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		originalState = oldState.clone();
//		originalState.getHexagon().setX(oldState.getHexagon().getX());
//		originalState.getHexagon().setY(oldState.getHexagon().getY());
//		try {
//			originalState.getHexagon().setR(oldState.getHexagon().getR());
//		} catch (IllegalRadiusException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		originalState.getHexagon().setBorderColor(oldState.getHexagon().getBorderColor());
//		originalState.getHexagon().setAreaColor(oldState.getHexagon().getAreaColor());
		oldState.getHexagon().setX(newState.getHexagon().getX());
		oldState.getHexagon().setY(newState.getHexagon().getY());
		try {
			oldState.getHexagon().setR(newState.getHexagon().getR());
		} catch (IllegalRadiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(newState.getHexagon().getBorderColor() == Color.BLACK && originalState.getHexagon().getBorderColor() != Color.BLACK) {
			oldState.getHexagon().setBorderColor(originalState.getHexagon().getBorderColor());
		} else {
			oldState.getHexagon().setBorderColor(newState.getHexagon().getBorderColor());
		}
		
		if(newState.getHexagon().getAreaColor()== Color.WHITE && originalState.getHexagon().getAreaColor()!= Color.WHITE) {
			oldState.getHexagon().setAreaColor(originalState.getHexagon().getAreaColor());
		} else {
			oldState.getHexagon().setAreaColor(newState.getHexagon().getAreaColor());
		}
//		oldState.getHexagon().setBorderColor(newState.getHexagon().getBorderColor());
//		oldState.getHexagon().setAreaColor(newState.getHexagon().getAreaColor());
//		AppModel.addShape(oldState);
	}

	@Override
	public void unexecute() {
		oldState.getHexagon().setX(originalState.getHexagon().getX());
		oldState.getHexagon().setY(originalState.getHexagon().getY());
		try {
			oldState.getHexagon().setR(originalState.getHexagon().getR());
		} catch (IllegalRadiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldState.getHexagon().setBorderColor(originalState.getHexagon().getBorderColor());
		oldState.getHexagon().setAreaColor(originalState.getHexagon().getAreaColor());
	}

	@Override
	public String toString() {
		return "Updated->"+oldState.toString()+"->"+newState.toString();
	}
	
}
