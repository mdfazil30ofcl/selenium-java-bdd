package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import pages.TestExecutor;

public class ChangeServerDateTimeUtils extends TestExecutor {

	String currentServerHostname, psCommand, psServCommand, Tenants;
	public boolean isW32TimeDeRegistered = false;

	public ChangeServerDateTimeUtils() {
		String url = new PropertyReader().readProperty("URL");
		currentServerHostname = common.getStringBetweenChar(url, "//", "/");
		psCommand = userDir + "/resources/PSTools/psexec.exe \\\\" + currentServerHostname;
		psServCommand = userDir + "/resources/PSTools/psservice.exe \\\\" + currentServerHostname;
	}

	public void setDateAndTimeInServer(String dateAndTime) {
		String str[] = dateAndTime.split(";");
		setDateInServer(str[0]);
		setTimeInServer(str[1]);
	}

	public void runPSCommand(String command) {
		try {
			Runtime.getRuntime().exec(command, null, null).waitFor(20, TimeUnit.SECONDS);
		} catch (IOException e) {
			System.out.println("Exception occured while setting date in server");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println(
					"Interrupted Exception occured while waiting for process to terminate with command " + command);
			e.printStackTrace();
		}
		System.out.println("Executed Command: " + command);
	}

	public int runPSCommandReturnExitCode(String command) throws InterruptedException {
		int exitValue = -8888;
		try {
			Process proc1 = Runtime.getRuntime().exec(command, null, null);
			proc1.waitFor(10, TimeUnit.SECONDS);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc1.getErrorStream()));
			exitValue = proc1.exitValue();
			System.out.println("Exit Value: " + exitValue);
			String s = null;
			String output = "";
			while ((s = stdInput.readLine()) != null) {
				output = output + s + "\n";
			}
			while ((s = stdError.readLine()) != null) {
				output = output + s + "\n";
			}
			System.out.println("Output: \n" + output.replace("\n", " ").replace("\r", " "));
		} catch (IOException e) {
			System.out.println("Exception occured while setting date in server");
			e.printStackTrace();
		}
		return exitValue;
	}

	public void setDateInServer(String date) {
		String command = psCommand + getDateToSetInServer(date);
		runPSCommand(command);
	}

	public void setTimeInServer(String time) {
		String command = psCommand + getTimeToSetInServer(time);
		runPSCommand(command);
	}

	public String getTimeToSetInServer(String time) {
		return " cmd /c \"time " + time + "\"";
	}

	public String getDateToSetInServer(String date) {
		return " cmd /c \"date " + date + "\"";
	}

	public void startService(String serviceName) {
		String command = psServCommand + " start " + serviceName;
		runPSCommand(command);
	}

	public void stopService(String serviceName) {
		String command = psServCommand + " stop " + serviceName;
		runPSCommand(command);
	}

	public void restartService(String serviceName) {
		System.out.println("Service Name: " + serviceName);
		String command = psServCommand + " restart " + serviceName;
		runPSCommand(command);
	}

	public void restartServiceIfNotStarted(String serviceName) throws InterruptedException {
		System.out.println("Service Name: " + serviceName);
		String command = psServCommand + " restart " + serviceName;
		String errorChkCommand = psCommand + " cmd /c w32tm /query /status";
		if (runPSCommandReturnExitCode(errorChkCommand) == -2147023834) {
			runPSCommand(command);
		} else {
			System.out.println("Seems service is up and running, hence skipping restart service operation");
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		/*
		 * csdt.setDateInServer("22-07-19"); Thread.sleep(1000);
		 * csdt.setTimeInServer("10:00:00.00"); Thread.sleep(1000);
		 * csdt.setDateAndTimeInServer("28-07-19;09:00:00.00"); Thread.sleep(1000);
		 * csdt.startService("LCMITREngine"); Thread.sleep(1000);
		 */
	}

	public void deRegisterW32TimeService() {
		System.out.println("Deregistered W32Time in server with below commands");		
		String command = psCommand + " cmd /c net stop w32time";
		runPSCommand(command);
		command = psCommand + " cmd /c w32tm.exe /unregister";
		runPSCommand(command);
		isW32TimeDeRegistered = true;
	}

	public void registerW32TimeService() {
		System.out.println("Registered W32Time in server with below commands");		
		String command = psCommand + " cmd /c w32tm.exe /register";
		runPSCommand(command);
		common.sleep(1000);
		command = psCommand + " cmd /c net start w32time";
		runPSCommand(command);
		isW32TimeDeRegistered = false;
	}

	public void registerW32TimeServiceIfNotRegistered() throws InterruptedException {
		String command = psCommand + " cmd /c w32tm.exe /register";
		String errorChkCommand = psCommand + " cmd /c w32tm /query /status";
		if (runPSCommandReturnExitCode(errorChkCommand) == -2147023836) {
			System.out.println("Registered W32Time in server with below commands");
			runPSCommand(command);
			command = psCommand + " cmd /c net start w32time";
			runPSCommand(command);
		} else {
			System.out.println("Seems service is already registered, hence skipping register service operation");
		}
	}
}
