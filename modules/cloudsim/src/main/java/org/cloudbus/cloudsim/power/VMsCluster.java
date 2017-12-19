package org.cloudbus.cloudsim.power;
import com.opencsv.bean.CsvBindByName;
public class VMsCluster {
	@CsvBindByName
	private String MachineID;
	
	@CsvBindByName
	private int Cluster;
	
	
	public String getMachineID() {
		return MachineID;
	}
	
	public int getCluster() {
		return Cluster;
	}
	
	public void setMachineID(String MachineID) {
		this.MachineID=MachineID;
	}
	
	public void setCluster(int Cluster) {
		this.Cluster=Cluster;
	}
}
