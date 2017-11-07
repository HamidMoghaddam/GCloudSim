package org.cloudbus.cloudsim.examples.power.gcluser;

import java.util.List;


import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.opencsv.bean.CsvToBeanBuilder;

public class GClusterConstants {
	public final static int NUMBER_OF_HOSTS = 1000;
	

	public final static double SCHEDULING_INTERVAL = 300;
	public final static double SIMULATION_LIMIT = 24 * 60 * 60;

	public final static int CLOUDLET_LENGTH	= 2500 * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_PES	= 1;
	
	/*
	 * VM instance types:
	 *   The Google Cluster has specific VM CPU and Memory configurations for each VMs so this function load 
	 *   the VM configurations from a CSV file
	 *
	 */
	public final static int VM_BW		= 100000; // 100 Mbit/s
	public final static int VM_SIZE		= 2500; // 2.5 GB
	public static List<VMsType> VMTypes() throws FileNotFoundException{
		String MachineFiles="Machine.csv";
		String inputFolder = GClusterConstants.class.getClassLoader().getResource("workload/GCluster/").getPath();
		String fileName=inputFolder+MachineFiles;
		List<VMsType> beans = new CsvToBeanBuilder<VMsType>(new FileReader(fileName)).withType(VMsType.class).build().parse();        		           
        return beans;   
	}
	

	/*
	 * Host types:
	 *   HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB)
	 *   HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB)
	 *   We increase the memory size to enable over-subscription (x4)
	 */
	public final static int HOST_TYPES	 = 2;
	public final static int[] HOST_MIPS	 = { 1860, 2660 };
	public final static int[] HOST_PES	 = { 2, 2 };
	public final static int[] HOST_RAM	 = { 4096, 4096 };
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 GB

	public final static PowerModel[] HOST_POWER = {
		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075()
	};
}
