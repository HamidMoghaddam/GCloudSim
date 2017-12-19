package org.cloudbus.cloudsim.power;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import java.util.stream.Collectors;

import org.cloudbus.cloudsim.Vm;


import com.opencsv.bean.CsvToBeanBuilder;


public class PowerVmSelectionPolicyClusterCorrelation extends PowerVmSelectionPolicy {
	/** The fallback VM selection policy to be used when
     * the  Maximum Correlation policy doesn't have data to be computed. */
	private PowerVmSelectionPolicy fallbackPolicy;
	
	private static final Map<Integer,Integer> vmsClusterID=new HashMap<Integer,Integer>();
	/**
	 * Instantiates a new PowerVmSelectionPolicyMaximumCorrelation.
	 * 
	 * @param fallbackPolicy the fallback policy
	 */
	public PowerVmSelectionPolicyClusterCorrelation(final PowerVmSelectionPolicy fallbackPolicy) {
		super();
		readClusters();
		setFallbackPolicy(fallbackPolicy);
	}

	@Override
	public Vm getVmToMigrate(PowerHost host) {
		
		List<PowerVm> migratableVms = getMigratableVms(host);
		if (migratableVms.isEmpty()) {
			return null;
		}
		Map<Integer,Vm> vmMigratableMap=getMigratableVMsClusters(migratableVms);
		Set<Integer> keySet = vmMigratableMap.keySet();
		Map<Integer,Integer> allVmsMap= new HashMap<Integer,Integer>(getVMsClusterID());
		//allVmsMap=getVMsClusterID();
		allVmsMap.keySet().retainAll(keySet);
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (Integer c : allVmsMap.values()) {
		    int value = counts.get(c) == null ? 0 : counts.get(c);
		    counts.put(c, value + 1);
		}
		Optional<Entry<Integer, Integer>> highCluster=counts.entrySet()
        .stream()
        .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
        .findFirst();
		int ClusterID=highCluster.get().getKey();
		Vm vmToMigrate=getVmMinimumMemory(vmMigratableMap,allVmsMap,ClusterID);
		return vmToMigrate;
	}
	private Vm getVmMinimumMemory(Map<Integer,Vm> vmMigratableMap,Map<Integer,Integer> allVmsMap,int selectedCluster) {
		Vm vmToMigrate = null;
		List<Vm> listMigratableVMs=new ArrayList<Vm>();
		for(Map.Entry<Integer,Vm> entry : vmMigratableMap.entrySet()) {
			if (allVmsMap.get(entry.getKey())==selectedCluster) {
				listMigratableVMs.add(entry.getValue());
			}
		}
		double minMetric = Double.MAX_VALUE;
		for (Vm vm : listMigratableVMs) {
			if (vm.isInMigration()) {
				continue;
			}
			double metric = vm.getRam();
			if (metric < minMetric) {
				minMetric = metric;
				vmToMigrate = vm;
			}
		}
		
		return vmToMigrate;
		
	}
	private Map<Integer,Vm> getMigratableVMsClusters(final List<PowerVm> vmList) {
		Map<Integer,Vm> migratableVMs=new HashMap<Integer,Vm>();
		for (Vm vm : vmList) {
			if (vm.isInMigration()) {
				continue;
			}
			migratableVMs.put(vm.getId(), vm);
		}
		return migratableVMs;
	}
	private void readClusters() {
		String MachineFiles="PlanetLabClusters.csv";
		String clusterFolderName= PowerVmSelectionPolicyClusterCorrelation.class.getClassLoader().getResource("workload/cluster/").getPath();
		String fileName=clusterFolderName+MachineFiles;
		String inputFolderName = PowerVmSelectionPolicyClusterCorrelation.class.getClassLoader().getResource("workload/planetlab/").getPath();
		String workload = "FiveDays"; // PlanetLab workload
		File inputFolder = new File(inputFolderName+workload);
		File[] files = inputFolder.listFiles();
		
		
		try {
			List<VMsCluster> vmClusters=new CsvToBeanBuilder<VMsCluster>(new FileReader(fileName)).withType(VMsCluster.class).build().parse();
			for (int i = 0; i < files.length; i++) {
				String VMname=files[i].getName();
				List<VMsCluster> VMType=vmClusters.stream().filter(a->Objects.equals(a.getMachineID(),VMname)).collect(Collectors.toList());
				
				int vmClusterID= VMType.get(0).getCluster();
				vmsClusterID.put(i, vmClusterID);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Map<Integer,Integer> getVMsClusterID(){
		return vmsClusterID;
	}
	/**
	 * Sets the fallback policy.
	 * 
	 * @param fallbackPolicy the new fallback policy
	 */
	public void setFallbackPolicy(final PowerVmSelectionPolicy fallbackPolicy) {
		this.fallbackPolicy = fallbackPolicy;
	}

	
	
}
