package org.freeplane.view.swing.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

interface Drawable{
	void draw(Graphics2D g, NodeView nodeView, Rectangle r);
}

class DrawableNothing implements Drawable{
	public void draw(Graphics2D g, NodeView nodeView, Rectangle r) {
	}
}
abstract class DrawableShape implements Drawable{
	private final Color fillColor;

	public DrawableShape(Color fillColor) {
		this.fillColor = fillColor;
	}
	public void draw(Graphics2D g, NodeView nodeView, Rectangle r) {
		final Color color = g.getColor(); 
		final Color edgeColor = nodeView.getEdgeColor();
		final Shape shape = getShape(r);
		g.setColor(getFillColor(nodeView));
		g.fill(shape);
		g.setColor(edgeColor);
		drawShape(g, shape, r, nodeView);
		g.setColor(color);
	}
	protected void drawShape(Graphics2D g, final Shape shape, Rectangle r, NodeView nodeView) {
		g.draw(shape);
	}
	abstract Shape getShape(Rectangle r);
	protected Color getFillColor(NodeView nodeView) {
		return fillColor;
	}
}

class DrawableEllipse extends DrawableShape{
	public DrawableEllipse(Color fillColor) {
		super(fillColor);
	}
	Shape getShape(Rectangle r){
		return new Ellipse2D.Float(r.x, r.y, r.width, r.height);
	}
}

class FoldingCircle extends DrawableEllipse{
	final private boolean folded;
	final private boolean hiddenChild;

	public FoldingCircle(Color fillColor, boolean folded, boolean hiddenChild) {
		super(fillColor);
		this.folded  = folded;
		this.hiddenChild =hiddenChild;
	}

	@Override
	protected void drawShape(Graphics2D g, Shape shape, Rectangle r, NodeView nodeView) {
		super.drawShape(g, shape, r, nodeView);
		if(nodeView.isRoot() & ! folded)
			return;
		if(! nodeView.getMainView().getMouseArea().equals(MouseArea.FOLDING))
			g.setColor(nodeView.getEdgeColor());
		else
			g.setColor(super.getFillColor(nodeView));
		if(! hiddenChild)
			g.drawLine(r.x + r.width / 4, r.y + r.height / 2, r.x + r.width * 3/ 4, r.y + r.height / 2);
		if(folded || hiddenChild)
			g.drawLine(r.x + r.width / 2, r.y + r.height / 4, r.x + r.width / 2, r.y + r.height * 3 / 4);
	}

	@Override
	protected Color getFillColor(NodeView nodeView) {
		if(nodeView.getMainView().getMouseArea().equals(MouseArea.FOLDING)){
			return Color.GRAY;
		}
		return super.getFillColor(nodeView);
	}
	
}

class DrawableTriangle extends DrawableShape{
	public DrawableTriangle(Color fillColor) {
		super(fillColor);
	}
	Shape getShape(Rectangle r){
		final Polygon polygon = new Polygon();
		polygon.addPoint(r.x, r.y);
		polygon.addPoint(r.x + r.width, r.y);
		polygon.addPoint(r.x + (r.width + 1) / 2, r.y + r.height);
		polygon.addPoint(r.x + r.width / 2, r.y + r.height);
		polygon.addPoint(r.x, r.y);
		return polygon;
	}
}

public enum FoldingMark implements Drawable{
	UNFOLDED(new DrawableNothing()), ITSELF_FOLDED(new DrawableEllipse(Color.WHITE)), UNVISIBLE_CHILDREN_FOLDED(new DrawableEllipse(Color.GRAY)), 
	SHORTENED(new DrawableTriangle(Color.WHITE)), 
	FOLDING_CIRCLE_FOLDED(new FoldingCircle(Color.WHITE, true, false)), FOLDING_CIRCLE_UNFOLDED(new FoldingCircle(Color.WHITE, false, false)),
	FOLDING_CIRCLE_HIDDEN_CHILD(new FoldingCircle(Color.WHITE, false, true));
	final Drawable drawable;

	FoldingMark(Drawable drawable){
		this.drawable = drawable;
	}
	
	public void draw(Graphics2D g, NodeView nodeView, Rectangle r) {
		drawable.draw(g, nodeView, r);
	}

}