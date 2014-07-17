/**
 * 
 */
package jmafiosi;

import java.awt.Color;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Ulf Lotzmann, Zheng Zhou
 *
 */
public class Shop {
	private int index;
	private Capos capo;
	
	public Capos getCapo() {
		return capo;
	}

	public void setCapo(Capos capo) {
		this.capo = capo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	private double capableToPayExtorsionMoney;
	
	
	public Shop(ContinuousSpace<Object> space, Grid<Object> grid, double capableToPayExtorsionMoney){
		this.space =space;
		this.grid =grid;
		this.capableToPayExtorsionMoney = capableToPayExtorsionMoney;
	}
	
	public double payExtorsion(Soldiers s) {
		// get the capo, to whom the soldier belongs
		Capos c = s.getMycapo();
		
		// get the capo, to whom the current shop belongs
		Capos c_shop = getCapo();
		
		// determine whether the capo of the soldier is same as the one of the current shop
		boolean hasSameCapo = c_shop == c;
						
		if (hasSameCapo) {
			
			System.out.println("[Shop] has the same capo as the soldier");
			
		}
		else{
			// when not the same family, than report to capos			
			System.out.println("[Shop] has the not same capo as the soldier");
			c_shop.onForeignerExtorteOwnShop(index, c.getIndex(), s);
		}
		
	
		
			
		
		//  amount of money might be dependent on the (family of the) soldier asking for money (just an idea...)
//		System.out.println("[Shop " + index +"]Before extorsion: $" + capableToPayExtorsionMoney);
//		double extorted = capableToPayExtorsionMoney * 0.01;
		double extorted = capableToPayExtorsionMoney;
//		capableToPayExtorsionMoney = capableToPayExtorsionMoney * 0.99;
//		System.out.println("[Shop " + index +"]After extorsion: $" + capableToPayExtorsionMoney);
		return extorted;
		
	}
	public String toString() {
		return "Shop #" + index;
	}
	

}
