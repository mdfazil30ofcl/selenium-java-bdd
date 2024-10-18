package utils;

import java.io.*;

public class CopyFileUtils {
	public static void copyFile(String srcFile, String destFile) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(new File(srcFile));
			output = new FileOutputStream(new File(destFile));
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			System.out.println("File Copied to : " + destFile);
		} catch (IOException e) {
			System.out.println("Something went wrong in accessing files, please validate the file paths: ");
			System.out.println("Source File path: " + srcFile);
			System.out.println("Destination File path: " + destFile);
		} finally {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		copyFile(args[0], args[1]);
	}
}
