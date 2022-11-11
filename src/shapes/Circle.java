package shapes;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JOptionPane;

import exceptions.IllegalRadiusException;



public class Circle extends SurfaceShape{
	protected Point center;
	protected int r;
	protected boolean selected;
	private boolean potvrda;


	public boolean isPotvrda() {
		return potvrda;
	}

	public void setPotvrda(boolean potvrda) {
		this.potvrda = potvrda;
	}
	

	//-----------------------------------------------------KONSTRUKTORI-----------------------------------------------------
	public Circle() {

	}

	public Circle(Point center, int r) {

		this.center = center;
		try {
			setR(r);
		} catch (IllegalRadiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Circle(Point center, int r, Color outlineColor, Color fillColor) throws IllegalRadiusException {

		this.center = center;
		setR(r);
		setOutlineColor(outlineColor);
		setFillColor(fillColor);
	}

	public Circle(Point center, int r, boolean selected) throws IllegalRadiusException {
		this(center, r);
		this.selected = selected;
	}


	//-----------------------------------------------------KONSTRUKTORI-----------------------------------------------------

	//-----------------------------------------------------TOSTRING,CONTAINS,DRAW,COMPARETO,AREA,MOVEBY-----------------------------------------------------
	public String toString() {
		return "Circle: radius=" + r + "; x=" + center.getX() + "; y=" + center.getY() + "; edge color=" + getOutlineColor().toString().substring(14).replace('=', '-') + "; area color=" + getFillColor().toString().substring(14).replace('=', '-');
	}

	public boolean contains(int x, int y) {
		double temp = center.distance(x, y);
		return temp <= r;
	}
	public Circle clone() {
    	try {
			return new Circle(center.clone(), r, getOutlineColor(), getFillColor());
		} catch (IllegalRadiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	public void draw(Graphics g) {
			g.setColor(getFillColor());
			g.fillOval(center.getX()-r, center.getY()-r, 2*r-1, 2*r-1);
			g.setColor(getOutlineColor());
		g.drawOval(center.getX()-r, center.getY()-r, 2*r, r+r);
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(center.getX() - r - 3, center.getY() - 3, 6, 6);
			g.drawRect(center.getX() + r - 3, center.getY() - 3, 6, 6);
			g.drawRect(center.getX() - 3, center.getY() - r - 3, 6, 6);
			g.drawRect(center.getX() - 3, center.getY() + r - 3, 6, 6);
			g.drawRect(center.getX() - 3, center.getY() - 3, 6, 6);
		}
	}

	   @Override
		public boolean equals(Object obj) {
			if (obj instanceof Circle) {
				Circle circle = (Circle) obj;
				return center.equals(circle.getCenter()) && r == circle.getR();
			}
			return false;
		}
	
	public int compareTo(Object o) {
		if(o instanceof Circle)
			return (int) ((this.area())-((Circle)o).area());
		return 0;
	}

	public double area() {
		return r*r*Math.PI;
	}

	public void moveBy(int x, int y) {
		center.moveBy(x, y);
	}
	//-----------------------------------------------------TOSTRING,CONTAINS,DRAW,COMPARETO,AREA,MOVEBY-----------------------------------------------------

	//-----------------------------------------------------GET SET METODE-----------------------------------------------------
	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) throws IllegalRadiusException{
		if(r > 0)
		{
			this.r = r;
		}
		else
		{
			throw new IllegalRadiusException("Radius can not be a negative number!");
		}
	}
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	//-----------------------------------------------------GET SET METODE-----------------------------------------------------

}
