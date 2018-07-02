package org.cloudbus.cloudsim.examples.power.gcluser;

import java.io.IOException;



public class LrMmt {
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = true;
		String inputFolder = LrMmt.class.getClassLoader().getResource("workload/GCluster/splittedPlatform2").getPath();
		String outputFolder = "/Users/hamid.moghaddam/Documents/PhD/output";
		String vmAllocationPolicy = "lr"; // Local Regression (LR) VM allocation policy
		String vmSelectionPolicy = "mmt"; // Minimum Migration Time (MMT) VM selection policy
		String parameter = "1.2"; // the safety parameter of the LR policy

		for (int i=0;i<29;i++){
			String workload=Integer.toString(i);
			new GClusterRunner(
					enableOutput,
					outputToFile,
					inputFolder,
					outputFolder,
					workload,
					vmAllocationPolicy,
					vmSelectionPolicy,
					parameter);
		}
	}
}
