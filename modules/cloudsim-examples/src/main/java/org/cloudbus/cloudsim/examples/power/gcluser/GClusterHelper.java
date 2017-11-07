package org.cloudbus.cloudsim.examples.power.gcluser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelGClusterInMemory;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerVm;

public class GClusterHelper {
	private final Map<Integer,String> VMsMap;
	private int brokerId;
	private String inputFolderName;
	public GClusterHelper(int brokerId, String inputFolderName) {
		this.VMsMap=new HashMap<Integer,String>();
		this.setBrokerId(brokerId);
		this.setInputFolderName(inputFolderName);
	}
	/**
	 * Creates the cloudlet list Google Cluster.
	 * 
	 * @param brokerId the broker id
	 * @param inputFolderName the input folder name
	 * @return the list
	 * @throws FileNotFoundException the file not found exception
	 */
	
	public List<Cloudlet> createCloudletListGCluster()
			throws FileNotFoundException {
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		long fileSize = 300;
		long outputSize = 300;
		UtilizationModel utilizationModelNull = new UtilizationModelNull();
		
		File inputFolder = new File(getInputFolderName());
		File[] files = inputFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			Cloudlet cloudlet = null;
			try {
				cloudlet = new Cloudlet(
						i,
						GClusterConstants.CLOUDLET_LENGTH,
						GClusterConstants.CLOUDLET_PES,
						fileSize,
						outputSize,
						new UtilizationModelGClusterInMemory(
								files[i].getAbsolutePath(),
								GClusterConstants.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull);
				
				setVMsMap(i,files[i].getName());
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			cloudlet.setUserId(getBrokerId());
			cloudlet.setVmId(i);
			list.add(cloudlet);
		}

		return list;
	}
	/**
	 * Creates the vm list.
	 * 
	 * @param brokerId the broker id
	 * @param vmsNumber the vms number
	 * 
	 * @return the list< vm>
	 */
	public List<Vm> createVmList(int brokerId, Map<Integer, String> vmNames) {
		List<Vm> vms = new ArrayList<Vm>();
		List<VMsType> VMs = null;
		try {
			VMs = GClusterConstants.VMTypes();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=0; i < vmNames.size() ; i++ ) {
			String VM=vmNames.get(i);
			List<VMsType> VMType=VMs.stream().filter(a->Objects.equals(a.getMachineID(),VM)).collect(Collectors.toList());
			int VM_MIPS=(int) VMType.get(0).getCPU();
			int VM_RAM=(int) VMType.get(0).getRAM();
			int VM_PES=1;
			vms.add(new PowerVm(
					i,
					brokerId,
					VM_MIPS,
					VM_PES,
					VM_RAM,
					GClusterConstants.VM_BW,
					GClusterConstants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(VM_MIPS, VM_PES),
					GClusterConstants.SCHEDULING_INTERVAL));
		}
		
		return vms;
	}

	public int getBrokerId() {
		return brokerId;
	}
	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}
	public String getInputFolderName() {
		return inputFolderName;
	}
	public void setInputFolderName(String inputFolderName) {
		this.inputFolderName = inputFolderName;
	}
	public Map<Integer,String> getVMsMap() {
		return VMsMap;
	}
	public void setVMsMap(Integer i,String VM) {
		VMsMap.put(i,VM);
	}
}
