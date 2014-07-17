/**
 * 
 */
package jmafiosi;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.gis.GeographyParameters;
import repast.simphony.space.gis.SimpleAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repastcity3.environment.Building;
import repastcity3.environment.GISFunctions;
import repastcity3.environment.SpatialIndexManager;
import repastcity3.environment.contexts.BuildingContext;
import repastcity3.main.GlobalVars;

/**
 * @author Zheng, Ulf Lotzmann
 * 
 */
public class JZBuilder implements ContextBuilder<Object> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context
	 * .Context)
	 */
	@Override
	public Context build(Context<Object> context) {

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
				"network", context, true);
		netBuilder.buildNetwork();

		context.setId("jmafiosi");

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), 50,
				50);
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 50, 50));
		
		// Territorien definieren: in jede Zelle wird ein TerritoryMarker-Objekt mit der dem Territorium zugeordneten Farbe hinzugefügt;
		// Die Farbe dient auch bei der weiteren Initialisierung als Diskriminierungsmerkmal sowie wird für die Visualisierung benutzt!
		for(int x=0; x<50; x++){
			for(int y=0; y<50; y++){
				Color c;
				if (x < 25) {
					if (y < 25) {
						c = Color.BLUE;
					}
					else {
						c = Color.RED;
					}
				}
				else {
					if (y < 25) {
						c = Color.GREEN;
					}
					else {
						c = Color.YELLOW;
					}
				}
				TerritoryMarker tm = new TerritoryMarker(space, grid, c);
				context.add(tm);
				space.moveTo(tm, x + 0.5, y + 0.5);
			}	
		}
		
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		
		 int SoldiersCount = (int)params.getValue("SoldiersCount");
//		int SoldiersCount = 80;
		for (int i = 0; i < SoldiersCount; i++) { 
			Soldiers soldier = new Soldiers(space, grid, 0.5, 1.002);
			soldier.setIndex(i + 1);
			context.add(soldier);
//			space.moveByDisplacement(soldier, 0.5, 0.5);
			NdPoint pt = space.getLocation(soldier);
			space.moveTo(soldier, ((int)pt.getX()) + 0.5, ((int)pt.getY()) + 0.5);
		}
		int shopCount = (int)params.getValue("shopCount");
	    double capableToPayExtorsionMoney = (double)params.getValue("capableToPayExtorsionMoney");	
//		int shopCount = 1000;
		for (int i = 0; i < shopCount; i++) {
			Shop shop = new Shop(space, grid, capableToPayExtorsionMoney);
			shop.setIndex(i + 1);
			context.add(shop);
//			space.moveByDisplacement(shop, 0.5, 0.5);
			NdPoint pt = space.getLocation(shop);
			space.moveTo(shop, ((int)pt.getX()) + 0.5, ((int)pt.getY()) + 0.5);
		}
		
		
//		int JhCount = 16;
//		for (int i = 0; i < JhCount; i++) {
//			int money = RandomHelper.nextIntFromTo(4, 10);
//			context.add(new Jh(space, grid, money));
//		}
	   double incomeLowThreshold1 = (double)params.getValue("incomeLowThreshold1");	
	   double incomeHighThreshold1 = (double)params.getValue("incomeHighThreshold1");	
	   double probMultiplicatorIncomeLow1 =(double)params.getValue("probMultiplicatorIncomeLow1");
	   double probMultiplicatorIncomeHigh1 =(double)params.getValue("probMultiplicatorIncomeHigh1");
	   double sanctionProbability1 =(double)params.getValue("sanctionProbability1");
	   
	   double incomeLowThreshold2 = (double)params.getValue("incomeLowThreshold2");	
	   double incomeHighThreshold2 = (double)params.getValue("incomeHighThreshold2");	
	   double probMultiplicatorIncomeLow2 =(double)params.getValue("probMultiplicatorIncomeLow2");
	   double probMultiplicatorIncomeHigh2 =(double)params.getValue("probMultiplicatorIncomeHigh2");
	   double sanctionProbability2 =(double)params.getValue("sanctionProbability2");
	   
	   double incomeLowThreshold3 = (double)params.getValue("incomeLowThreshold3");	
	   double incomeHighThreshold3 = (double)params.getValue("incomeHighThreshold3");	
	   double probMultiplicatorIncomeLow3 =(double)params.getValue("probMultiplicatorIncomeLow3");
	   double probMultiplicatorIncomeHigh3 =(double)params.getValue("probMultiplicatorIncomeHigh3");
	   double sanctionProbability3 =(double)params.getValue("sanctionProbability3");
	   
	   double incomeLowThreshold4 = (double)params.getValue("incomeLowThreshold4");	
	   double incomeHighThreshold4 = (double)params.getValue("incomeHighThreshold4");	
	   double probMultiplicatorIncomeLow4 =(double)params.getValue("probMultiplicatorIncomeLow4");
	   double probMultiplicatorIncomeHigh4 =(double)params.getValue("probMultiplicatorIncomeHigh4");
	   double sanctionProbability4 =(double)params.getValue("sanctionProbability4");
	   
		Capos capo1 = new Capos(space, grid, incomeLowThreshold1, incomeHighThreshold1, probMultiplicatorIncomeLow1, probMultiplicatorIncomeHigh1,sanctionProbability1);
		capo1.setIndex(1);
		context.add(capo1);
		space.moveTo(capo1, 12.5, 12.5);
		//grid.moveTo(capo, 0, 0);
		
		Capos capo2 = new Capos(space, grid, incomeLowThreshold2, incomeHighThreshold2, probMultiplicatorIncomeLow2, probMultiplicatorIncomeHigh2,sanctionProbability2);
		capo2.setIndex(2);
		context.add(capo2);
		space.moveTo(capo2, 37.5, 12.5);
		//grid.moveTo(capo, 25, 0);
		
		Capos capo3 = new Capos(space, grid, incomeLowThreshold3, incomeHighThreshold3, probMultiplicatorIncomeLow3, probMultiplicatorIncomeHigh3,sanctionProbability3);
		capo3.setIndex(3);
		context.add(capo3);
		space.moveTo(capo3, 12.5, 37.5);
		
		
		Capos capo4 = new Capos(space, grid, incomeLowThreshold4, incomeHighThreshold4, probMultiplicatorIncomeLow4, probMultiplicatorIncomeHigh4,sanctionProbability4);
		capo4.setIndex(4);
		context.add(capo4);
		space.moveTo(capo4, 37.5, 37.5);
		
		// random pick a mandamento from the 4 capos above.
		Random r = new Random();
		int capoIndex = r.nextInt(4);
		Mandamento m = new Mandamento();
		
		// UL: bugfix for non-working mandamento sanction graph
		context.add(m);
		
		if (capoIndex == 0) {
			m.setCapo(capo1);
		} else if (capoIndex == 1) {
			m.setCapo(capo2);			
		} else if (capoIndex == 2) {
			m.setCapo(capo3);
		} else if (capoIndex == 3) {
			m.setCapo(capo4);
		}
		capo1.setMandamento(m);
		capo2.setMandamento(m);
		capo3.setMandamento(m);
		capo4.setMandamento(m);	
		

		//}
//		int MandamentoCount = 1;
//		for (int i = 0; i < MandamentoCount; i++) {	
//			 Capos.nextInt(5);
//			context.add(new Mandamento(space, grid));
		
			
//		}
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
//			grid.getCellAccessor().
		}
		capo1.linkin();
		capo2.linkin();
		capo3.linkin();
		capo4.linkin();
		
		

//		BuildingContext buildingContext = new BuildingContext();
//		Geography<Building> buildingProjection = GeographyFactoryFinder
//				.createGeographyFactory(null).createGeography(
//						GlobalVars.CONTEXT_NAMES.BUILDING_GEOGRAPHY,
//						buildingContext,
//						new GeographyParameters<Building>(
//
//								new SimpleAdder<Building>()));
//		String buildingFile = "D:\\RepastSimphony-2.0\\workspace\\jmafiosi\\shapedata\\map.shp";
//		try {
//			GISFunctions.readShapefile(Building.class, buildingFile,
//					buildingProjection, buildingContext);
//			context.addSubContext(buildingContext);
//			SpatialIndexManager.createIndex(buildingProjection, Building.class);
//		} catch (MalformedURLException e) {
//
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		}

		return context;
	}

	private void Label(String string) {
		System.out.println(string);

	}

}
