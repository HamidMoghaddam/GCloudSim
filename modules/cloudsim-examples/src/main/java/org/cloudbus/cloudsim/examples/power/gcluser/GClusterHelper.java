package org.cloudbus.cloudsim.examples.power.gcluser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.examples.power.Constants;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

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
	 * Creates the cloudlet list Google Clusters
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
	 * @param vmNames the vms name
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
	/**
	 * Creates the host list.
	 *
	 * @param hostsNumber the hosts number
	 *
	 * @return the list< power host>
	 */
	public static List<PowerHost> createHostList(int hostsNumber) {
		List<PowerHost> hostList = new ArrayList<PowerHost>();
		for (int i = 0; i < hostsNumber; i++) {
			int hostType = i % GClusterConstants.HOST_TYPES;

			List<Pe> peList = new ArrayList<Pe>();
			for (int j = 0; j < GClusterConstants.HOST_PES[hostType]; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(GClusterConstants.HOST_MIPS[hostType])));
			}

			hostList.add(new PowerHostUtilizationHistory(
					i,
					new RamProvisionerSimple(GClusterConstants.HOST_RAM[hostType]),
					new BwProvisionerSimple(GClusterConstants.HOST_BW),
					GClusterConstants.HOST_STORAGE,
					peList,
					new VmSchedulerTimeSharedOverSubscription(peList),
					GClusterConstants.HOST_POWER[hostType]));
		}
		return hostList;
	}
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	public static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new PowerDatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return broker;
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
