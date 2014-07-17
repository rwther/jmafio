/**
 * 
 */
package jmafiosi;


import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;
import repast.simphony.query.space.grid.GridCell;

/**
 * @author Zheng, Ulf Lotzmann
 * 
 * This class might become a super class for moveable/mobile agents...
 *
 */
public abstract class Jz {
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	private boolean moved;
	public double money;
	
	public Jz(ContinuousSpace<Object> space, Grid<Object> grid){
		this.space =space;
		this.grid =grid;
	}
	
	public ContinuousSpace<Object> getSpace(){
		return this.space;
	}
	
	public Grid<Object> getGrid(){
		return this.grid;
	} 
	@ScheduledMethod(start =1, interval =1)	
	public void step(){	
	GridPoint pt = grid.getLocation(this);
	
	// call method that performs actions for the current position/cell
	doActionForCurrentPosition(pt);
	
	
	// perform random wald movement step
	GridCellNgh<Jh> nghCreator =new GridCellNgh<Jh>(grid, pt, Jh.class, 1, 1);
	List<GridCell<Jh>> gridCells = nghCreator.getNeighborhood(true);
	SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
	
//	GridPoint pointWithMostJH = null;
//	int maxCount =-1;
//	for (GridCell<Jh> cell:gridCells){
//		if (cell.size() > maxCount){
//			pointWithMostJH = cell.getPoint();
//			maxCount = cell.size();
//		}
//	}
//	moveTowards(pointWithMostJH);
    moveTowards(gridCells.get(0).getPoint());
    
//	Capos.linkin(space, grid);
	}
	
	
	/**
	 * Abstract method for implementing actions to be performed at each simulation step. To be overridden in subclasses...  
	 * @param pt
	 */
	protected abstract void doActionForCurrentPosition(GridPoint pt);
	
	public void moveTowards(GridPoint pt){
	
		if (!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space.getLocation(this);
			
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle =SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 1, angle, 0);
			myPoint = space.getLocation(this);
			
			space.moveTo(this, ((int)myPoint.getX()) + 0.5, ((int)myPoint.getY()) + 0.5);
			
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			
			moved = false;
		}
	}
	public void mord(){		
		GridPoint pt = grid.getLocation(this);
		List<Object> soldiers = new ArrayList<Object>();
		for (Object obj:grid.getObjectsAt(pt.getX(), pt.getY())){
			if (obj instanceof Soldiers){
				soldiers.add(obj);
			}
		}
			if (soldiers.size()>0){
			int index = RandomHelper.nextIntFromTo(0, 5);	
			Object obj = soldiers.get(index);
			NdPoint spacePt = space.getLocation(obj);
			Context<Object> context = ContextUtils.getContext(obj);
			//context.remove(obj);
			//Soldiers soldiers = new Soldiers(space, grid);
			//context.add(soldiers);
			space.moveTo(soldiers, spacePt.getX(), spacePt.getY());
			grid.moveTo(soldiers, pt.getX(), pt.getY());
			
			Network<Object> net =(Network<Object>)context.getProjection("network");
			net.addEdge(this, soldiers);
			
		}

	}
	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
}

