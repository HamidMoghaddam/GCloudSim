package org.cloudbus.cloudsim.power;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;


import com.opencsv.CSVReader;
public class PowerVmAllocationPolicyMigrationMLs extends PowerVmAllocationPolicyMigrationAbstract {

	/** The map between VMs id and VMs prediction */
	private Map<Integer,String[]> vmsMap=new HashMap<Integer,String[]>();
	/** The scheduling interval that defines the periodicity of VM migrations. */
	private double schedulingInterval;

	/** The safety parameter in percentage (at scale from 0 to 1).
         * It is a tuning parameter used by the allocation policy to 
         * estimate host utilization (load). The host overload detection is based
         * on this estimation.
         * This parameter is used to tune the estimation
         * to up or down. If the parameter is set as 1.2, for instance, 
         * the estimated host utilization is increased in 20%, giving
         * the host a safety margin of 20% to grow its usage in order to try
         * avoiding SLA violations. As this parameter decreases, more
         * aggressive will be the consolidation (packing) of VMs inside a host,
         * what may lead to optimization of resource usage, but rising of SLA 
         * violations. Thus, the parameter has to be set in order to balance
         * such factors.
         */
	private double safetyParameter;

	/** The fallback VM allocation policy to be used when
         * the Local REgression over utilization host detection doesn't have
         * data to be computed. */
	private PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy;

	/**
	 * Instantiates a new PowerVmAllocationPolicyMigrationLocalRegression.
	 * 
	 * @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 * @param schedulingInterval the scheduling interval
	 * @param fallbackVmAllocationPolicy the fallback vm allocation policy
	 * @param utilizationThreshold the utilization threshold
	 */
	public PowerVmAllocationPolicyMigrationMLs(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy,
			double safetyParameter,
			double schedulingInterval,
			PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy,
			double utilizationThreshold) {
		super(hostList, vmSelectionPolicy);
		setSafetyParameter(safetyParameter);
		setSchedulingInterval(schedulingInterval);
		setFallbackVmAllocationPolicy(fallbackVmAllocationPolicy);
		creatVMsMap();
	}

	/**
	 * Instantiates a new PowerVmAllocationPolicyMigrationLocalRegression.
	 * 
	 * @param hostList the host list
	 * @param vmSelectionPolicy the vm selection policy
	 * @param schedulingInterval the scheduling interval
	 * @param fallbackVmAllocationPolicy the fallback vm allocation policy
	 */
	public PowerVmAllocationPolicyMigrationMLs(
			List<? extends Host> hostList,
			PowerVmSelectionPolicy vmSelectionPolicy,
			double safetyParameter,
			double schedulingInterval,
			PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy) {
		super(hostList, vmSelectionPolicy);
		setSafetyParameter(safetyParameter);
		setSchedulingInterval(schedulingInterval);
		setFallbackVmAllocationPolicy(fallbackVmAllocationPolicy);
		creatVMsMap();
	}
	/** 
	 * Create VMs id and name map
	 */
	protected void creatVMsMap()  {
		Map<String,Integer> vmsNameID=new HashMap<String,Integer>();
		String inputFolderName= PowerVmSelectionPolicyClusterCorrelation.class.getClassLoader().getResource("workload/planetlab/FifthDay").getPath();
		String MachineFiles="PlanetLabPredictions.csv";
		String clusterFolderName= PowerVmSelectionPolicyClusterCorrelation.class.getClassLoader().getResource("workload/cluster/").getPath();
		String fileName=clusterFolderName+MachineFiles;
		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();
		// Guaranty to have same order of VMs in memory
		Arrays.sort(files);
		for (int i = 0; i < files.length; i++) {
			vmsNameID.put(files[i].getName(), i);
		}
		try {
			CSVReader reader = new CSVReader(new FileReader(fileName));
			List<String[]> allRows = reader.readAll();
			for(String[] row : allRows){
				int vmsID=vmsNameID.get(row[0]);
				String[] newArray = Arrays.copyOfRange(row, 1, row.length);
				setVmsMap(vmsID,newArray);
		        //System.out.println(newArray);
		     }
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Checks if a host is over utilized.
	 * 
	 * @param host the host
	 * @return true, if is host over utilized; false otherwise
	 */
	@Override
	protected boolean isHostOverUtilized(PowerHost host) {
		
		int length = 20; // we use 20 to make the regression responsive enough to latest values
		double predictedUtilization = 0;
		double hostMips =host.getTotalMips();
		for(PowerVm vm :  host.<PowerVm> getVmList()) {
			int timeStep=vm.getUtilizationHistory().size();
			if (timeStep <length) {
				return getFallbackVmAllocationPolicy().isHostOverUtilized(host);
			}
			int vmID=vm.getId();
			String[] predictions= getVmsMap().get(vmID);
			//Next step prediction
			double prediction=Double.parseDouble(predictions[timeStep+1]);
			predictedUtilization+=prediction* vm.getMips() / hostMips;
		}
		predictedUtilization *= getSafetyParameter();
		return predictedUtilization >= 1;
	
	}
	
	/**
	 * Sets the scheduling interval.
	 * 
	 * @param schedulingInterval the new scheduling interval
	 */
	protected void setSchedulingInterval(double schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}

	/**
	 * Gets the scheduling interval.
	 * 
	 * @return the scheduling interval
	 */
	protected double getSchedulingInterval() {
		return schedulingInterval;
	}

	/**
	 * Sets the fallback vm allocation policy.
	 * 
	 * @param fallbackVmAllocationPolicy the new fallback vm allocation policy
	 */
	public void setFallbackVmAllocationPolicy(
			PowerVmAllocationPolicyMigrationAbstract fallbackVmAllocationPolicy) {
		this.fallbackVmAllocationPolicy = fallbackVmAllocationPolicy;
	}

	/**
	 * Gets the fallback vm allocation policy.
	 * 
	 * @return the fallback vm allocation policy
	 */
	public PowerVmAllocationPolicyMigrationAbstract getFallbackVmAllocationPolicy() {
		return fallbackVmAllocationPolicy;
	}

	public double getSafetyParameter() {
		return safetyParameter;
	}

	public void setSafetyParameter(double safetyParameter) {
		this.safetyParameter = safetyParameter;
	}

	public Map<Integer,String[]> getVmsMap() {
		return vmsMap;
	}

	public void setVmsMap(Integer key,String[] vmsPrediction) {
		this.vmsMap.put(key, vmsPrediction);
	}

}
