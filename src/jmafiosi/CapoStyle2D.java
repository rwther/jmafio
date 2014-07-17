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

public class CapoStyle2D extends DefaultStyleOGL2D {


	@Override
	public Color getColor(Object o) {
		Capos agent = (Capos)o;
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
			path.moveTo(-10, -10);
			path.lineTo(10,  -10);
			path.lineTo(0, 10);
			path.closePath();
			spatial = shapeFactory.createShape(path);//createCircle(10, 100);
		}
		return spatial;
	}


}
