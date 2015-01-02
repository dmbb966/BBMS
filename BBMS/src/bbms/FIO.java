package bbms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FIO {
	static Charset cSet = Charset.forName("US-ASCII");
	int readFinger = 0;

	public static boolean BWriteFile(Path p, String s, StandardOpenOption opt) {
		try (BufferedWriter writer = Files.newBufferedWriter(p, cSet, opt)) {
			writer.write(s + "\n", 0, s.length() + 1);
		} catch (IOException e) {
			System.err.format("IO Error writing to file: %s%n", e);
			return false;
		}
		
		return true;
	}

	public static File newFile(String s) {
		File f = new File(s);
		try {
			f.createNewFile();
		} catch (IOException e) {
			System.err.format("IO Exception when creating new file: %s%n", e);
			return null;
		}
		
		return f;
	}

	public static String BReadFile(Path p, String s) {
		String output = "";
		
		try (BufferedReader reader = Files.newBufferedReader(p, cSet)) {
			char c = ' ';
			
			while (c != '|') {
				c = (char) reader.read();
				output += c;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return output;
		}
		return output;
	}
	
	public static boolean LoadFile(Path p) {
		// TODO: Reset old environment
		
		try {
			BufferedReader reader = Files.newBufferedReader(p,  cSet);
			String readL;
			while ((readL = reader.readLine()) != null) {				
				if (readL.startsWith("#")) GUI_NB.GCO("Comment follows: " + readL);
				else GUI_NB.GCO(readL);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static boolean overwriteFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.TRUNCATE_EXISTING);
	}

	public static boolean appendFile(Path p, String s) {
		return BWriteFile(p, s, StandardOpenOption.APPEND);
	}
}
