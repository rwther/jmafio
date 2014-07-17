/**
 * 
 */
package jmafiosi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.collections.UnremoveableIterator;

/**
 * @author Zheng, Ulf Lotzmann
 * 
 */
public class Capos extends Jh {
	
	
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	public long punishedNumber, punishmentCount;
	
	// list of soldiers assigned to Capo
	private List<Object> soldiers = new ArrayList<Object>();

	// list of shops assigned to Capo
	private List<Object> shops = new ArrayList<Object>();

	public List<Object> getShops() {
		return shops;
	}

	// discriminating color of Capo's family
	private Color familyColor = Color.BLACK;

	// parameter: thresholds for controlling probability to sanction soldiers
	private double incomeLowThreshold, incomeHighThreshold;
	// parameter: probability multiplicator for low/high threshold
	private double probMultiplicatorIncomeLow, probMultiplicatorIncomeHigh;
	//parameter: probability to sanction soldiers
	private double sanctionProbability;

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

	// belongs to whom, who is the mandamento
	private Mandamento mandamento;

	public Mandamento getMandamento() {
		return mandamento;
	}

	public void setMandamento(Mandamento mandamento) {
		this.mandamento = mandamento;
	}

	public Capos(ContinuousSpace<Object> space, Grid<Object> grid,
			double incomeLowThreshold, double incomeHighThreshold,
			double probMultiplicatorIncomeLow,
			double probMultiplicatorIncomeHigh,
			double sanctionProbability) {
		super(space, grid, 0);
		this.incomeLowThreshold = incomeLowThreshold;
		this.incomeHighThreshold = incomeHighThreshold;
		this.probMultiplicatorIncomeLow = probMultiplicatorIncomeLow;
		this.probMultiplicatorIncomeHigh = probMultiplicatorIncomeHigh;
		this.sanctionProbability = sanctionProbability;
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {

		// generating some numerical output
		sanctionsPerSoldier = 1.0 * sanctionCount / soldiers.size();
		proportionOfNotPermittedExtiorsions = 1.0 * notPermittedExtorsionCount
				/ extorsionCount;

		periodicIncomePerSoldier = cummulatedIncome / soldiers.size();
		cummulatedIncome = 0;
		extorsionCount = 0;
		sanctionCount = 0;
		notPermittedExtorsionCount = 0;
		punishmentCount = punishedNumber;
		punishedNumber = 0;

		// learning --> preliminary! 
		if (period > 0) {
			if (periodicIncomePerSoldier < incomeLowThreshold) {
				sanctionProbability *= probMultiplicatorIncomeLow;
			} else if (periodicIncomePerSoldier > incomeHighThreshold) {
				sanctionProbability *= probMultiplicatorIncomeHigh;
			}
			sanctionProbability = sanctionProbability > 1.0 ? 1.0
					: sanctionProbability;
		}
		period++;

		// delete network
		Context<Object> context = ContextUtils.getContext(this);
		Network<Object> net = (Network<Object>) context
				.getProjection("network");
		net.removeEdges();
	}

	/**
	 * Initialisation of Capo agent
	 */
	public void linkin() {

		System.out.println("Objects assigned to Capo #" + index + ", "
				+ this.toString() + ": ");

		GridPoint pt = grid.getLocation(this);

		// finde Farbe des Territoriums auf dem der Capo sich befindet und setze
		// eigene Familienfarbe entsprechend
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof TerritoryMarker) {
				familyColor = ((TerritoryMarker) obj).getTerritoryColor();
				break;
			}
		}

		// Ordne alle Soldaten und Shops auf dem Territorium dem Capo zu
		Iterable allObjects = grid.getObjects();
		for (Object object : allObjects) {

			// finde Farbe des Territoriums auf dem sich das aktuelle Objekt
			// befindet
			Color c = Color.BLACK;
			GridPoint objLocation = grid.getLocation(object);
			for (Object obj : grid.getObjectsAt(objLocation.getX(),
					objLocation.getY())) {
				if (obj instanceof TerritoryMarker) {
					c = ((TerritoryMarker) obj).getTerritoryColor();
					break;
				}
			}

			// Wenn Object auf Territorium der Familie (entspr. gleiche Farbe),
			// dann...
			if (familyColor.equals(c)) {

				// System.out.print("   Soldiers: ");
				// Soldat zuordnen
				if (object instanceof Soldiers) {
					Soldiers soldier = (Soldiers) object;
					soldiers.add(soldier);
					soldier.setMycapo(this);
					System.out.print(soldier.toString()+ " ");
				}

				// System.out.print("\n   Shops: ");
				// Shop zuordnen
				if (object instanceof Shop) {
					Shop shop = (Shop) object;
					shop.setCapo(this);
					shops.add(shop);
				    System.out.print(shop.toString()+" ");
				}
			}

		}

		// for (int x=pt.getX(); x<pt.getX()+25; x++)
		// for(int y=pt.getY(); y<pt.getY()+25;y++){
		// Object obj=grid.getObjectsAt(x,y);
		// // System.out.print(obj.toString()+" ");
		// if (obj instanceof UnremoveableIterator){
		// UnremoveableIterator iter = (UnremoveableIterator)obj;
		// while (iter.hasNext()){
		// Object element =iter.next();
		// if (element instanceof Soldiers) {
		// Soldiers s = (Soldiers) element;
		// soldiers.add(s);
		// s.setMycapo(this);
		// }
		// System.out.print(element.toString()+" ");
		// }
		// //
		// // System.out.print(obj.toString()+" ");
		// }
		// }
		System.out.println();
		// for (Object obj:grid.getObjectsAt(pt.getX(), pt.getY())){
		// if (obj instanceof Soldiers){
		// soldiers.add(obj);
		// }
		// }
		// if (soldiers.size()>0){
		// int index = RandomHelper.nextIntFromTo(0, 5);
		// Object obj = soldiers.get(index);
		// ArrayList<Soldiers> soldiers1 ;
		// NdPoint spacePt = space.getLocation(obj);
		// Context<Object> context = ContextUtils.getContext(obj);
		// space.moveTo(soldiers, spacePt.getX(), spacePt.getY());
		// grid.moveTo(soldiers, pt.getX(), pt.getY());
		//
		//
		//
		// Network<Object> net
		// =(Network<Object>)context.getProjection("network");
		// net.addEdge(this, soldiers);
		//
		// }

	}

	public void giveExtorsionMoney(double amount, Soldiers soldier, Shop shop,
			Color territory) {
		// store money

		// distribute money
		// 50 persent for capos, then rest for solidiers

		System.out.println("capo #" + this.getIndex() + "cummulatedIncome = "
				+ cummulatedIncome + "extorsionCount = " + extorsionCount);

		extorsionCount++;

		cummulatedIncome += amount * 0.5;
		soldier.setMoney(soldier.getMoney() + amount * 0.5);
		System.out.println("capo #" + index + " has now: $" + cummulatedIncome);

		// draw edges between Capo and by own soldiers extorted shops
		Context<Object> context = ContextUtils.getContext(this);
		Network<Object> net = (Network<Object>) context
				.getProjection("network");
		net.addEdge(this, shop);

		// sanction if money from shop from other territory
		if (!territory.equals(familyColor)) {
			// decision process --> preliminary! --- TODO: could be implemented
			// with EMIL-S

			notPermittedExtorsionCount++;

			double rnd = RandomHelper.nextDoubleFromTo(0.0, 1.0);
			if (rnd <= sanctionProbability) {
				soldier.sanction(shop, territory);
				sanctionCount++;
			}

		}

		// notify the mandamento
		if (this != mandamento.getCapo()) {
			mandamento.onExtortMoney(index, cummulatedIncome);
		}
	}

	public double getPeriodicIncome() {
		return periodicIncomePerSoldier;
	}

	public double getPeriod() {
		return period;
	}

	public double getSanctionsPerSoldier() {
		return sanctionsPerSoldier;
	}

	public double getProportionOfNotPermittedExtiorsions() {
		return proportionOfNotPermittedExtiorsions;
	}

	public Color getFamilyColor() {
		return familyColor;
	}
    public long getPunishedNumber() {
		return punishmentCount;
		}  

	public void sanction(Soldiers s) {
		// punish all the soldiers form this capo instead of a single soldier
		// s.AcceptSanctionForExtortedShopOfOtherCapo();
		for (int i = 0; i < soldiers.size(); i++) {
			Soldiers so = (Soldiers) soldiers.get(i);
			so.AcceptSanctionForExtortedShopOfOtherCapo();
		}
	}
	
	public boolean trySanction(Soldiers s) {
		
		//probability for the sanction in display!set a new parameter!
		
		sanctionProbability = sanctionProbability * 1.1;
		sanctionProbability = sanctionProbability > 1.0 ? 1.0 : sanctionProbability;
		
		//sanctionProbability =;
		double rnd = RandomHelper.nextDoubleFromTo(0.0, 1.0);
		if (rnd <= sanctionProbability) {
			sanction(s);
			return true;
		}
		return false;
	}

	public void onForeignerExtorteOwnShop(int shop_index,
			int foreigner_capo_index, Soldiers s) {
		System.out.println("[Capo #" + index + "] knowns shop #" + shop_index
				+ " was robbed by a soldier of capo #" + foreigner_capo_index);
		// wait for another capo sanction
		// Queue<Event> & ArrayList<Event> or Queue/ Multithreads+ message
		// try{
		// Thread.currentThread().sleep(1000);
		// }
		// catch{Exception e};
		// report to Mendamento
		mandamento.onForeignerExtorteShop(foreigner_capo_index, s);
	}

	@Override
	public String toString() {
		return "Capo #" + index;
	}

}
