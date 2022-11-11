package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import exceptions.IllegalRadiusException;
public class Donut extends Circle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int innerRadius;
	private int outerRadius;
	private Color outlineColor;
	private Color fillColor;
	private boolean potvrda;
	
	public Color getOutlineColor() {
		return outlineColor;
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor; 
	}

	public boolean isPotvrda() {
		return potvrda;
	}

	public void setPotvrda(boolean potvrda) {
		this.potvrda = potvrda;
	}
	
	
	
	public int getOuterRadius() {
		return outerRadius;
	}

	public void setOuterRadius(int outerRadius) {
		this.outerRadius = outerRadius;
	}

	//-----------------------------------------------------KONSTRUKTORI-----------------------------------------------------
	public Donut() {

	}
	
	public Donut(Point center, int outerRadius, int innerRadius) throws IllegalRadiusException{
		super(center,outerRadius);
		if(innerRadius < outerRadius)
		{
			this.innerRadius = innerRadius;
			this.outerRadius = outerRadius;
		}
		else
		{
			throw new IllegalRadiusException("Inner radius can not be smaller than outer radius!");
		}
	}
	
	public Donut(Point center, int outerRadius, int innerRadius, Color outlineColor, Color fillColor) throws IllegalRadiusException{
		super(center,outerRadius);
		if(innerRadius < outerRadius)
		{
			this.innerRadius = innerRadius;
			this.outerRadius = outerRadius;
			this.outlineColor = outlineColor;
			this.fillColor = fillColor;
		}
		else
		{
			throw new IllegalRadiusException("Inner radius can not be smaller than outer radius!");
		}
	}

	public Donut(Point center, int outerRadius, int innerRadius, boolean selected) throws IllegalRadiusException {
		this(center, outerRadius, innerRadius);
		this.selected = selected;
	}
	//-----------------------------------------------------KONSTRUKTORI-----------------------------------------------------
	//-----------------------------------------------------TOSTRING, AREA, CONTAINS, DRAW-----------------------------------------------------
	public String toString() {
		return "Donut: radius=" + outerRadius + "; x=" + center.getX() + "; y=" + center.getY() + "; edge color=" + getOutlineColor().toString().substring(14).replace('=', '-') + "; area color=" + getFillColor().toString().substring(14).replace('=', '-') + "; inner radius=" + innerRadius;
	}

	public double area() {
		return super.area() - (innerRadius * innerRadius * Math.PI);
	}
	public Donut clone() {
    	try {
			return new Donut(center.clone(), outerRadius, innerRadius, getOutlineColor(), getFillColor());
		} catch (IllegalRadiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
	
	public boolean equals(Object obj) {
		if (obj instanceof Donut) {
			Donut d = (Donut) obj;
			if (this.getCenter().equals(d.getCenter()) && this.getOuterRadius() == d.getOuterRadius() && this.innerRadius == d.getInnerRadius()) {
				return true;
			} 
			else {
				return false;
			}
		} 
		else {
			return false;
		}
	}
	
	public boolean contains(int x, int y) {
		return super.contains(x, y) && center.distance(x, y) > innerRadius;
	}

	public void draw(Graphics g) {
	/*	g.setColor(outlineColor);
		g.drawOval(center.getX() - innerRadius, center.getY() - innerRadius, 2 * innerRadius, 2 * innerRadius);
		g.drawOval(center.getX()-r, center.getY()-r, 2*r, 2*r);
	*/	
		Color color = new Color (255.0f / 255.0f, 255.0f / 255.0f, 221.0f / 255.0f, 0.0f); 
		g.setColor(color);
		
		g.fillOval(this.getCenter().getX() - this.getInnerRadius(), this.getCenter().getY() - this.getInnerRadius(),
				this.getInnerRadius() * 2, this.getInnerRadius() * 2);
		
		if (getFillColor() != null) {
			g.setColor(getFillColor());
			for(int i = getInnerRadius(); i < getOuterRadius(); i++) {
				g.drawOval(this.getCenter().getX() - i,
						  this.getCenter().getY() - i, i * 2,
						  i * 2);		
			}	
		}
		
		
		if (getOutlineColor() != null)
			g.setColor(getOutlineColor());
		g.drawOval(this.getCenter().getX() - this.getOuterRadius(), this.getCenter().getY() - this.getOuterRadius(),
				this.getOuterRadius() * 2, this.getOuterRadius() * 2);
		g.drawOval(this.getCenter().getX() - this.getInnerRadius(), this.getCenter().getY() - this.getInnerRadius(),
				this.getInnerRadius() * 2, this.getInnerRadius() * 2);
		
		g.setColor(new Color(0, 0, 0));
		if (isSelected()) {
			g.setColor(Color.BLUE);
			g.drawRect(this.getCenter().getX() - innerRadius - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() + innerRadius - 3, this.getCenter().getY() - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - innerRadius - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + innerRadius - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - outerRadius - 3 , this.getCenter().getY() , 6, 6);
			g.drawRect(this.getCenter().getX() + outerRadius - 3 , this.getCenter().getY() , 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() - outerRadius - 3, 6, 6);
			g.drawRect(this.getCenter().getX() - 3, this.getCenter().getY() + outerRadius - 3, 6, 6);
		}
	}
	//-----------------------------------------------------TOSTRING, AREA, CONTAINS, DRAW-----------------------------------------------------

	//-----------------------------------------------------GET SET METODE-----------------------------------------------------
	public int getInnerRadius() {
		return innerRadius;
	}

	public void setInnerRadius(int innerRadius) {
		this.innerRadius = innerRadius;
	}
	//-----------------------------------------------------GET SET METODE-----------------------------------------------------
}
