import java.util.Properties;

import javax.naming.Context;

import com.siperian.sif.client.EjbSiperianClient;
import com.siperian.sif.client.SiperianClient;
import com.siperian.sif.message.Field;
import com.siperian.sif.message.Record;
import com.siperian.sif.message.RecordKey;
import com.siperian.sif.message.SiperianObjectType;
import com.siperian.sif.message.mrm.PutRequest;
import com.siperian.sif.message.mrm.PutResponse;

public class Put {
	public static void main(String args[]) {
		new Put().execute();
	}

	private void execute() {
		Properties properties = new Properties();
		properties.put(SiperianClient.SIPERIANCLIENT_PROTOCOL, "ejb");
		properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		properties.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.interfaces");
		properties.put(Context.PROVIDER_URL, "remote://localhost:4447");
		properties.put("jboss.naming.client.ejb.context", true);
		properties.put("siperian-client.orsId", "pdb-MDM_SAMPLE");
		//properties.put("siperian-client.username", "admin");
		//properties.put("siperian-client.password", "admin");
		try {
			EjbSiperianClient sipClient = (EjbSiperianClient) SiperianClient.newSiperianClient(properties);
			System.out.println("EjbSiperianClientDemo.main() client created successfuly");

			
				PutRequest putRequest = new PutRequest();
				RecordKey recKey = new RecordKey();
				putRequest.setUsername("idd/admin");
				recKey.setSourceKey("JavaTest1");
				recKey.setRowid("761923");
				recKey.setSystemName("Admin");
				putRequest.setRecordKey(recKey);
				Record record = new Record();
				record.setSiperianObjectUid(SiperianObjectType.BASE_OBJECT.makeUid("C_PARTY"));
				record.setField(new Field("ORGANIZATION_NAME", "Testing"));
				record.setField(new Field("PARTY_TYPE", "Person"));
				
				//record.setField(new Field("CUSTOMER_ID", "PUT" + i));
				putRequest.setRecord(record);
				PutResponse putResponse = (PutResponse) sipClient.process(putRequest);

				System.out.println("------------>" + putResponse.getMessage());
				System.out.println(putResponse.getRecordKey().getRowid());
				
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
