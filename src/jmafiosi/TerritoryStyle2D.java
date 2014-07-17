package jmafiosi;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Ulf Lotzmann
 */

public class TerritoryStyle2D extends DefaultStyleOGL2D {



	private int brighter(int c) {
		int res = c + 160;
		if (res > 255) res = 255;
		return res;
	}

	@Override
	public Color getColor(Object o) {
		TerritoryMarker agent = (TerritoryMarker)o;
		Color ac = agent.getTerritoryColor();
		Color cellColor = new Color(brighter(ac.getRed()), brighter(ac.getGreen()), brighter(ac.getBlue()));
		return cellColor;
	}

	private ShapeFactory2D shapeFactory;

	@Override
	public void init(ShapeFactory2D factory) {
		this.shapeFactory = factory;
	}

	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {
			spatial = shapeFactory.createRectangle(13, 13);
		}
		return spatial;
	}


}
