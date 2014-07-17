/**
 * 
 */
package jmafiosi;

import java.awt.Color;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Ulf Lotzmann
 *
 */
public class TerritoryMarker {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private Color territoryColor;
	
	public Color getTerritoryColor() {
		return territoryColor;
	}

	private Capos owner;
	
	public Capos getOwner() {
		return owner;
	}

	public void setOwner(Capos owner) {
		this.owner = owner;
	}

	public TerritoryMarker(ContinuousSpace<Object> space, Grid<Object> grid, Color territoryColor){
		this.space =space;
		this.grid =grid;
		this.territoryColor = territoryColor;
	}


}
