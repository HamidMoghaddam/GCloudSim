package org.cloudbus.cloudsim.power.models;
/**
 * The power model of an Supermicro Computer Inc. 1022G-NTF (2 x [AMD Opteron 6380 HE 2500 MHz, 32 cores], 64GB).<br/>
 * <a http://www.spec.org/power_ssj2008/results/res2012q4/power_ssj2008-20121015-00557.html">
 * http://www.spec.org/power_ssj2008/results/res2012q4/power_ssj2008-20121015-00557.html</a>
 * 
 **/
public class PowerModelSpecPowerSupermicro1022GSAmd6380 extends PowerModelSpecPower {
	/** 
     * The power consumption according to the utilization percentage. 
     * @see #getPowerData(int) 
     */
	private final double[] power = { 77.9, 115, 137, 158, 181, 205, 230, 256, 275, 293, 308 };
	
	@Override
	protected double getPowerData(int index) {
		return power[index];
	}

}
