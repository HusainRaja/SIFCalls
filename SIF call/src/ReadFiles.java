import java.io.BufferedReader;

import java.io.FileReader;

import com.informatica.mdm.cs.server.invoker.StepContextImpl;
import com.informatica.mdm.spi.cs.StepContext;

public class ReadFiles  {
	public static void main(String args[]) {
		try {
			FileReader file = new FileReader("C:\\Users\\hraja\\Desktop\\export");
			BufferedReader br = new BufferedReader(file);
			String rowid = "-1";
			while((rowid = br.readLine()) != null) {
				System.out.println(rowid);
			}
			//start of testing for diff issue, added mdm-spi for testing
			/*
			StepContext stepContext;
			stepContext.getRepository().getHelperContext()
			*/
			//end of testing
			
			br.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
