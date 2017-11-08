package org.cloudbus.cloudsim.examples.power.gcluser;

import java.util.List;


import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerSupermicro1022GSAmd6262;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerSupermicro1022GSAmd6380;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.opencsv.bean.CsvToBeanBuilder;

public class GClusterConstants {
	public final static int NUMBER_OF_HOSTS = 1200;
	

	public final static double SCHEDULING_INTERVAL = 300;
	public final static double SIMULATION_LIMIT = 24 * 60 * 60;

	public final static int CLOUDLET_LENGTH	= 2500 * (int) SIMULATION_LIMIT;
	public final static int CLOUDLET_PES	= 1;
	
	/*
	 * VM instance types:
	 *   The Google Cluster has specific VM CPU and Memory configurations for each VMs so this function load 
	 *   the VM configurations from a CSV file. The CPU and memory usages are normalized by Google so please 
	 *   check the VMsType class for Maximum CPU and memory
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
	 *   Supermicro Computer Inc. 1022G-NTF (2 x [AMD Opteron 6262 HE 1600 MHz, 32 cores], 32GB)
	 *   Supermicro Computer Inc. 1022G-NTF (2 x [AMD Opteron 6380 HE 2500 MHz, 32 cores], 64GB)
	 *   We increase the memory size to enable over-subscription (x4)
	 */
	public final static int HOST_TYPES	 = 2;
	public final static int[] HOST_MIPS	 = { 1600, 2500 };
	public final static int[] HOST_PES	 = { 32, 32};
	public final static int[] HOST_RAM	 = { 32768,65536 };
	public final static int HOST_BW		 = 1000000; // 1 Gbit/s
	public final static int HOST_STORAGE = 1000000; // 1 GB

	public final static PowerModel[] HOST_POWER = {
		new PowerModelSpecPowerSupermicro1022GSAmd6262(),
		new PowerModelSpecPowerSupermicro1022GSAmd6380(),
		};
}
