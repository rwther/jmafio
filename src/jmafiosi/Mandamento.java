/**
 * 
 */
package jmafiosi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

/**
 * @author Zheng
 *
 */
public class Mandamento {
	private Capos capo;
	// variables for statistics
	
	private int extorsionCount, sanctionCount, notPermittedExtorsionCount,punishedNumber;
	
	private int period;
	
	private double sanctionsPerCapo = 1.0;
	
	
	
	public Capos getCapo() {
		return capo;
	}


	public void setCapo(Capos capo) {
		this.capo = capo;
	}


	public void onExtortMoney(int index, double cummulatedIncome) {
		System.out.println("[Mandamento] knows that capo #" + index + " has ATM $" + cummulatedIncome);
	}


	public void onForeignerExtorteShop(int foreigner_capo_index, Soldiers s) {
		System.out.println("[Mandamento] knows that a soldier #" + s.getIndex() + " of capo #" + foreigner_capo_index + " has robbed a shop of another capo.");
		
		//if (!(s.getMycapo() == this.getCapo()) && (!s.getMycapo().trySanction(s)))
		s.getMycapo().punishedNumber++;
		if (!s.getMycapo().trySanction(s))
		{
		    
			// 即打手越界后首先是小头目来惩罚只有小头目不惩罚的时候，老大才惩罚小头
			//s.getMycapo().sanction(s);
//			s.getMycapo().punishedNumber++;
			System.out.println("[Mandamento] give that capo #" + foreigner_capo_index + " sanction.");
		}		
		
		// UL: bugfix for non-working mandamento sanction graph --> code moved to step() method
		//sanctionsPerCapo = 1.0 * sanctionCount/4;
		//System.out.println("Sanction per Capo is now "+ sanctionsPerCapo);
		
		sanctionCount++;
		
	}
	public double getPeriod() {
		return period;
	}
	public double getSanctionsPerCapo() {
		return sanctionsPerCapo;
	}
	
	@ScheduledMethod(start =1, interval =100)	
	public void step(){	
		// UL: bugfix for non-working mandamento sanction graph
		period++;
		
		sanctionsPerCapo = sanctionCount; //100.0 * sanctionCount/4;
		System.out.println("Sanction per Capo is now "+ sanctionsPerCapo);
		sanctionCount = 0;
		
		
	}
	 

	@Override
	public String toString() {
		return "Mandamento (" + capo.toString() + ")";
	}
	
		

	
}
