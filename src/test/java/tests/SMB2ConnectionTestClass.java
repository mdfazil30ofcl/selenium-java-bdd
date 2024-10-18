package tests;

//Please refer https://stackoverflow.com/questions/41494357/accessing-smb2-1-or-smb3-share-from-java

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;

public class SMB2ConnectionTestClass {

	public static void main(String[] args) throws IOException {
		String sourcePath = "C:\\file.txt";
		FileInputStream fileInputStream = new FileInputStream(new File(sourcePath));
		byte[] buf = new byte[16 * 1024 * 1024];
		int len;
		while ((len = fileInputStream.read(buf)) > 0) {
			upload("shared\\test.txt", buf, len);
		}
		fileInputStream.close();
	}

	// connection params
	static String sambaDomain = ""; // can be null
	static String sambaUsername = "";
	static String sambaPass = "";
	static String sambaIP = "10.10.10.10";
	static String sambaSharedPath = "Shared";

	public static void upload(String filename, byte[] bytes, int len) throws IOException {

		SmbConfig cfg = SmbConfig.builder().build();
		SMBClient client = new SMBClient(cfg);
		Connection connection = client.connect(sambaIP);
		Session session = connection
				.authenticate(new AuthenticationContext(sambaUsername, sambaPass.toCharArray(), sambaDomain));
		// ------------------
		// ------------------
		DiskShare share = (DiskShare) session.connectShare(sambaSharedPath);

		// this is com.hierynomus.smbj.share.File !
		com.hierynomus.smbj.share.File f = null;
		int idx = filename.lastIndexOf("/");

		// if file is in folder(s), create them first
		if (idx > -1) {
			String folder = filename.substring(0, idx);
			if (!share.folderExists(folder))
				share.mkdir(folder);
		}

		// I am creating file with flag FILE_CREATE, which will throw if file exists
		// already
		if (!share.fileExists(filename)) {
			f = share.openFile(filename, new HashSet<>(Arrays.asList(AccessMask.GENERIC_ALL)),
					new HashSet<>(Arrays.asList(FileAttributes.FILE_ATTRIBUTE_NORMAL)), SMB2ShareAccess.ALL,
					SMB2CreateDisposition.FILE_CREATE,
					new HashSet<>(Arrays.asList(SMB2CreateOptions.FILE_DIRECTORY_FILE)));
		} else {
			System.out
					.println("File with the name " + filename + " already exists in the folder " + share.getSmbPath());
		}
		if (f == null)
			System.out.println("No file present");
		OutputStream os = f.getOutputStream();
		os.write(bytes, 0, len);
		os.close();
		f.close();
	}

}
