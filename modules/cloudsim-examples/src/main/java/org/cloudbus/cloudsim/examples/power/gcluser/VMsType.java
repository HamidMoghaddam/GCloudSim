package org.cloudbus.cloudsim.examples.power.gcluser;
import com.opencsv.bean.CsvBindByName;
public class VMsType {
	@CsvBindByName
	private String MachineID;
	
	@CsvBindByName
	private double RAM;
	
	@CsvBindByName
	private double CPU;
	
	public String getMachineID() {
		return MachineID;
	}
	
	public double getRAM() {
		return RAM;
	}
	
	public double getCPU() {
		return CPU;
	}
	
	public void setMachineID(String MachineID) {
		this.MachineID=MachineID;
	}
	
	public void setRAM(double RAM) {
		int maxRAM=2048;
		this.RAM=RAM*maxRAM;
	}
	
	public void setCPU(double CPU) {
		int maxCPU=2500;
		this.CPU=CPU*maxCPU;
	}
}
