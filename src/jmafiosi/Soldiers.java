/**
 * 
 */
package jmafiosi;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.glassfish.external.probe.provider.annotations.Probe;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * @author Zheng, Ulf Lotzmann
 *
 */
public class Soldiers extends Jz{
	private int index;
	
	public int getIndex() {
		return index;
	}
	
	private Color territory;

	public void setIndex(int index) {
		this.index = index;
	}

	private Capos mycapo;
	
	// variables for statistics
	private double cummulatedIncome, periodicIncomePerSoldier;
	// variables for statistics
	private int extorsionCount, sanctionCount, notPermittedExtorsionCount;
	// period counter
	private int period;
	// variables for statistics
	private double sanctionsPerSoldier;
	// variables for statistics
	private double proportionOfNotPermittedExtiorsions;
	
	// probabilities (double values) to extort shops in different territories (key: color of territory)
	private Map<Color, Double> probablitiesForExtorsionInTerritory = new HashMap<Color, Double>();
	
	// parameter: probability for sanction learning and forgetting
	private double probMultiplicatorSanction, probMultiplicatorForgetting;
	

	public Soldiers(ContinuousSpace<Object> space, Grid<Object> grid, double probMultiplicatorSanction, double probMultiplicatorForgetting) {
		super(space, grid);
		
		this.probMultiplicatorSanction = probMultiplicatorSanction;
		this.probMultiplicatorForgetting = probMultiplicatorForgetting;
		
		
	}
	
	
	
	
	
	@Override
	protected void doActionForCurrentPosition(GridPoint pt) {
		
		// forgetting process: slightly increase probability of extorsion for all territories --- TODO: could be implemented with EMIL-S
		for (Map.Entry<Color, Double> probEntry : probablitiesForExtorsionInTerritory.entrySet()) {
			Double p = probEntry.getValue();
			p *= probMultiplicatorForgetting;
			p = p > 1.0 ? 1.0 : p;
			probEntry.setValue(p);
		}
		
		// determine current territory
		Shop shop = null;
		Color territoryForCurrentPosition = null;
		for (Object obj:grid.getObjectsAt(pt.getX(), pt.getY())){
			if (obj instanceof TerritoryMarker){
				territoryForCurrentPosition = ((TerritoryMarker) obj).getTerritoryColor();
			}
			else if (obj instanceof Shop) {
				shop = (Shop) obj;
				
			}
		}
		
		if (shop != null) {
			// try to extort shop
			
			// get probability for extorting shops in the current territory
			Double prob = probablitiesForExtorsionInTerritory.get(territoryForCurrentPosition);
			if (prob == null) {
				prob = 1.0;
				probablitiesForExtorsionInTerritory.put(territoryForCurrentPosition, prob);
			}
			
			// decision process --- TODO: could be implemented with EMIL-S
			double rnd = RandomHelper.nextDoubleFromTo(0.0, 1.0);
			if (rnd <= prob) {
				extortShop(shop, territoryForCurrentPosition);
			}
		}
	}


	private void extortShop(Shop shop, Color territory) {
		double income = shop.payExtorsion(this);
		System.out.println("soldiers " + index + " extroted money from shop: $" + income);
		mycapo.giveExtorsionMoney(income, this, shop, territory);
		
		// draw edges between soldiers and extorted shops
//		Context<Object> context = ContextUtils.getContext(this);
//		Network<Object> net =(Network<Object>)context.getProjection("network");
//		net.addEdge(shop, this);
	}
	
	public void sanction(Shop shop, Color territory) {
		this.territory = territory; 
		Double prob = probablitiesForExtorsionInTerritory.get(territory);
		if (prob != null) {
			Double newProb = prob * probMultiplicatorSanction;
			probablitiesForExtorsionInTerritory.put(territory, newProb);
		}
	}
	

	public Color getFamilyColor() {
		return mycapo != null ? mycapo.getFamilyColor() : Color.BLACK; 	
	}


	/**
	 * @param mycapo the mycapo to set
	 */
	public void setMycapo(Capos mycapo) {
		this.mycapo = mycapo;
	}

	/**
	 * @return the mycapo
	 */
	public Capos getMycapo() {
		return mycapo;
	}

	public void AcceptSanctionForExtortedShopOfOtherCapo() {
		Double prob = probablitiesForExtorsionInTerritory.get(territory);
		System.out.println("Soldier #" + this.getIndex() + ", old prob = " + prob + ", on territory " + territory);
		if (prob != null) {
			Double newProb = prob * 0.5;
						
			if (newProb < 0.0001) {
				newProb = 0.0001;
			}
			System.out.println("Soldier #" + this.getIndex() + ", new prob = " + newProb + ", on territory " + territory);
			probablitiesForExtorsionInTerritory.put(territory, newProb);
			
		}
		
	}
	
	public double getPeriod() {
		return period;
	}

	public double getSanctionsPerSoldier() {
		return sanctionsPerSoldier;
	}
	public double getPeriodicIncome() {
		return periodicIncomePerSoldier;
	}
	public double getProbMultiplicatorSanction() {
		return probMultiplicatorSanction;
	}
	
	public String toString() {
		return "Soldier #" + index;
	}

}
