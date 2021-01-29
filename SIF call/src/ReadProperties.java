import java.io.FileReader;
import java.util.Properties;

public class ReadProperties {
	public static void main (String args[]) {
		try {
			Properties config  = new Properties();
			FileReader file = new FileReader("C:\\Users\\hraja\\Desktop\\my.properties");
			config.load(file);
			System.out.println(config.getProperty("test.program"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
