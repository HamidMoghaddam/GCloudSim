package org.cloudbus.cloudsim.examples.power.gcluser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelGClusterInMemory;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.power.PowerVm;

public class GClusterHelper {
	private final Map<Integer, String> VMsMap;
	private int brokerId;
	private String inputFolderName;
	public GClusterHelper(int brokerId, String inputFolderName) {
		this.VMsMap= new HashMap<Integer, String>();
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
						Constants.CLOUDLET_LENGTH,
						Constants.CLOUDLET_PES,
						fileSize,
						outputSize,
						new UtilizationModelGClusterInMemory(
								files[i].getAbsolutePath(),
								Constants.SCHEDULING_INTERVAL), utilizationModelNull, utilizationModelNull);
				
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
	public static List<Vm> createVmList(int brokerId, Map<Integer, String> VMsMap) {
		List<Vm> vms = new ArrayList<Vm>();
		for (int i = 0; i < vmsNumber; i++) {
			int vmType = i / (int) Math.ceil((double) vmsNumber / Constants.VM_TYPES);
			vms.add(new PowerVm(
					i,
					brokerId,
					Constants.VM_MIPS[vmType],
					Constants.VM_PES[vmType],
					Constants.VM_RAM[vmType],
					Constants.VM_BW,
					Constants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]),
					Constants.SCHEDULING_INTERVAL));
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
	public Map<Integer, String> getVMsMap() {
		return VMsMap;
	}
	public void setVMsMap(Integer id,String VM) {
		VMsMap.put(id, VM);
	}
}
