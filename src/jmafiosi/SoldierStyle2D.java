package jmafiosi;

import java.awt.Color;
import java.awt.geom.GeneralPath;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * 
 * @author Ulf Lotzmann
 */

public class SoldierStyle2D extends DefaultStyleOGL2D {


	@Override
	public Color getColor(Object o) {
		Soldiers agent = (Soldiers)o;
		return agent.getFamilyColor();
	}

	private ShapeFactory2D shapeFactory;

	@Override
	public void init(ShapeFactory2D factory) {
		this.shapeFactory = factory;
	}

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {
			GeneralPath path = new GeneralPath();
			path.moveTo(-5, -5);
			path.lineTo(5,  -5);
			path.lineTo(0, 5);
			path.closePath();
			spatial = shapeFactory.createShape(path);//  createCircle(5, 100);
		}
		return spatial;
	}


}
