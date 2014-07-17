/**
 * 
 */
package jmafiosi;

import repast.simphony.space.grid.GridPoint;
import java.util.List;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;

import repast.simphony.util.SimUtilities;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

/**
 * @author Zheng
 *
 */
public class Jh {
	
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	private int money, startingMoney;
	public Jh(ContinuousSpace<Object> space, Grid<Object> grid, int money){
		this.space =space;
		this.grid= grid;
		this.money =startingMoney=money;
	}
	@Watch(watcheeClassName = "jmafiosi.Jz",
			watcheeFieldNames ="moved",
			query ="within_moore 1",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void run(){
//		GridPoint pt = grid.getLocation(this);
//		
//		GridCellNgh<Jz> nghCreator = new GridCellNgh<Jz>(grid, pt, Jz.class, 1, 1);
//		List<GridCell<Jz>> gridCells = nghCreator.getNeighborhood(true);
//		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());
//		
//		GridPoint pointWithLeastJz = null;
//		int minCount=Integer.MAX_VALUE;
//		for (GridCell<Jz> cell:gridCells) {
//			if(cell.size()< minCount){
//				pointWithLeastJz = cell.getPoint();
//				minCount = cell.size();
//			}
//		}
//		if (money >0) {
//			moveTowards(pointWithLeastJz);
//		} else {
//			money = startingMoney;
//		}
		}

	public void moveTowards(GridPoint pt){
		if (!pt.equals(grid.getLocation(this))){
			NdPoint myPoint = space.getLocation(this);
			//System.out.println(grid.getLocation(this).getX());
			NdPoint otherPoint = new NdPoint(pt.getX(), pt.getY());
			double angle = SpatialMath.calcAngleFor2DMovement(space, myPoint, otherPoint);
			space.moveByVector(this, 2, angle, 0);
			myPoint = space.getLocation(this);
			grid.moveTo(this, (int)myPoint.getX(), (int)myPoint.getY());
			money -- ;
		}
	}
}
