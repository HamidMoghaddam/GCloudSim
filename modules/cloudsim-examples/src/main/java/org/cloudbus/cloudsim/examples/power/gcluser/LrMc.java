package org.cloudbus.cloudsim.examples.power.gcluser;

import java.io.IOException;

public class LrMc {
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		boolean enableOutput = true;
		boolean outputToFile = false;
		String inputFolder = LrMmt.class.getClassLoader().getResource("workload/GCluster").getPath();
		String outputFolder = "/Users/hamid.moghaddam/Documents/PhD/output";
		String workload = "GoogleData"; // PlanetLab workload
		String vmAllocationPolicy = "lr"; // Local Regression (LR) VM allocation policy
		String vmSelectionPolicy = "mc"; // Maximum Correlation (MC) VM selection policy
		String parameter = "1.2"; // the safety parameter of the LR policy

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
