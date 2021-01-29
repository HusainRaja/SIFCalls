import java.io.BufferedReader;

import java.io.FileReader;
import java.util.Properties;

import com.siperian.sif.client.EjbSiperianClient;
import com.siperian.sif.client.HttpSiperianClient;
import com.siperian.sif.client.SiperianClient;
import com.siperian.sif.message.RecordKey;
import com.siperian.sif.message.RecordState;
import com.siperian.sif.message.SiperianObjectType;
import com.siperian.sif.message.mrm.SetRecordStateRequest;
import com.siperian.sif.message.mrm.SetRecordStateResponse;

public class SetCSI {
	
	
	Properties config = new Properties();
	FileReader rowidFile,propFile;
	BufferedReader br;
	SetRecordStateResponse response;
	String propertyPath;
	
	public static void main (String args[]) {
		new SetCSI(args[0]).execute();
		
	}
	
	
	
	public SetCSI(String arg) {
		propertyPath = arg;
		System.out.println("Path of the property file is " + arg);
		
	}
	

	
	
	private void execute() {
	
		readProperties();
		
		try {	
			
			SiperianClient client =  SiperianClient.newSiperianClient(config);
			rowidFile = new FileReader(config.getProperty("rowid_list.file.path"));
			br = new BufferedReader(rowidFile);
			SetRecordStateRequest request = new SetRecordStateRequest();
			RecordKey key = new RecordKey();
			String rowid;
		
			request.addRecordKey(key);
			
			request.setSiperianObjectUid(SiperianObjectType.BASE_OBJECT.makeUid(config.getProperty("base_object.name")));
			request.setRecordState(RecordState.NEWLY_LOADED);
		
			while((rowid = br.readLine()) != null) {
				key.setRowid(rowid);
				response = (SetRecordStateResponse) client.process(request);
				System.out.println("For rowid object - " + rowid + response.getMessage());
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void readProperties() {
		try {
			propFile = new FileReader(propertyPath);
			config.load(propFile);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
	}
}
