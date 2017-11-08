package org.cloudbus.cloudsim.power.models;
/**
 * The power model of an Supermicro Computer Inc. 1022G-NTF (2 x [AMD Opteron 6262 HE 1600 MHz, 32 cores], 32GB).<br/>
 * <a http://www.spec.org/power_ssj2008/results/res2011q4/power_ssj2008-20111021-00407.html">
 * http://www.spec.org/power_ssj2008/results/res2011q4/power_ssj2008-20111021-00407.html</a>
 * 
 **/
public class PowerModelSpecPowerSupermicro1022GSAmd6262 extends PowerModelSpecPower {
		/** 
	     * The power consumption according to the utilization percentage. 
	     * @see #getPowerData(int) 
	     */
	private final double[] power = { 70.3, 99.0, 115, 130, 143, 156, 169, 182, 193, 203, 213 };
	
	@Override
	protected double getPowerData(int index) {
		return power[index];
	}

}
