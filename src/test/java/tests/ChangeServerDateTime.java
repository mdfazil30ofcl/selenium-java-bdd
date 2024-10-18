package tests;

import java.io.*;
import java.util.concurrent.TimeUnit;

import pages.TestExecutor;

public class ChangeServerDateTime extends TestExecutor {

	public static void main(String[] args) throws IOException, InterruptedException {
		ChangeServerDateTime.runPSExecCommand();
	}

	public static void runPSExecCommand() throws IOException, InterruptedException {
		String currentServerHostname = "10.10.10.10";
		String psCommand = userDir + "/resources/PSTools/psexec.exe \\\\" + currentServerHostname;
		String psServCommand = userDir + "/resources/PSTools/psservice.exe \\\\" + currentServerHostname;
		psCommand = psCommand + " cmd /c w32tm /query /status";
		psServCommand = psServCommand + " start w32time";
		System.out.println("PSEXEC command: " + psCommand);
		System.out.println("PSSERVICE command: " + psServCommand);
		Process proc1 = Runtime.getRuntime().exec(psServCommand, null, null);
		proc1.waitFor(40, TimeUnit.SECONDS);		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc1.getErrorStream()));		
		int i = proc1.exitValue();
		System.out.println("exit value: " +i);
		String s = null;
		String output = "";
		while ((s = stdInput.readLine()) != null) {
			System.out.println("STD IN : "+s);
			output = output + s + "\n";
		}
		while ((s = stdError.readLine()) != null) {
			System.out.println("STD ERR : "+s);
			output = output + s + "\n";
		}
		System.out.println("Output: \n" + output);
		System.out.println("Done!!!");
	}
}
