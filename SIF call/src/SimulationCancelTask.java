import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import com.google.common.util.concurrent.SettableFuture;
import com.siperian.sif.client.EjbSiperianClient;
import com.siperian.sif.client.SiperianClient;
import com.siperian.sif.message.Field;
import com.siperian.sif.message.HubStateIndicator;
import com.siperian.sif.message.Record;
import com.siperian.sif.message.RecordKey;
import com.siperian.sif.message.RecordState;
import com.siperian.sif.message.SiperianObjectType;
import com.siperian.sif.message.SiperianObjectUidField;
import com.siperian.sif.message.SiperianRequest;
import com.siperian.sif.message.TaskData;
import com.siperian.sif.message.TaskMetaData;
import com.siperian.sif.message.TaskRecord;
import com.siperian.sif.message.XrefKey;
import com.siperian.sif.message.XrefResult;
import com.siperian.sif.message.mrm.GetRequest;
import com.siperian.sif.message.mrm.GetResponse;
import com.siperian.sif.message.mrm.GetTasksRequest;
import com.siperian.sif.message.mrm.GetTasksResponse;
import com.siperian.sif.message.mrm.RestoreRequest;
import com.siperian.sif.message.mrm.RestoreResponse;
import com.siperian.sif.message.mrm.SearchQueryRequest;
import com.siperian.sif.message.mrm.SearchQueryResponse;

public class SimulationCancelTask {
	public static void main(String args[]) {
		
		new SimulationCancelTask().execute();
	}
	
	boolean restoreFlag = false;

	private void execute() {
		Properties config = new Properties();		
		config.put("java.naming.factory.initial","org.wildfly.naming.client.WildFlyInitialContextFactory");
		config.put("java.naming.provider.url","remote://localhost:4447");
		config.put("siperian-client.protocol","ejb");
		config.put("java.naming.factory.url.pkgs","org.jboss.ejb.client.interfaces");
		config.put("jboss.naming.client.ejb.context","true");
		config.put("siperian-client.orsId","pdb-MDM_SAMPLE");
		config.put("siperian-client.username","admin");
		config.put("siperian-client.password","admin");
		
		EjbSiperianClient client = (EjbSiperianClient) SiperianClient.newSiperianClient(config);
		restore(client);
		RecordKey key = new RecordKey();
		key.setRowidXref("1");
		searchQuery(client);
		getTask(client);
		GetRequest getRequest = new GetRequest();
		getRequest.setRecordKey(key);
		getRequest.setSiperianObjectUid(SiperianObjectType.BASE_OBJECT.makeUid("C_PARTY"));
		
		GetResponse getResponse = (GetResponse) client.process(getRequest);
		Record record = new Record();
		ArrayList<Record> list = getResponse.getRecords();
		record = list.get(0);
		System.out.println(record.getField("HUB_STATE_IND").getBigIntegerValue().toString()+".");
		System.out.println(record.getField("HUB_STATE_IND").getBigIntegerValue().toString().compareTo("1"));
		if ((record.getField("HUB_STATE_IND").getBigIntegerValue().toString().compareTo("1")) == 0) {
			System.out.println("inside the if condition");
			restore(client);
		}
	}

	private void getTask(EjbSiperianClient client) {
		try {
			GetTasksRequest request = new GetTasksRequest();
			List<TaskMetaData> list = new ArrayList<TaskMetaData>();
			TaskMetaData taskMetaData = new TaskMetaData();
			taskMetaData.setTaskId("120004");
			list.add(taskMetaData);
			request.setTaskMetaData(list);
			GetTasksResponse response = (GetTasksResponse) client.process(request);
			List taskList = new ArrayList();
			taskList = response.getTaskList();
			System.out.println(response.getRecordCount());
			System.out.println(response.getMessage());
			System.out.println(taskList.size());
			TaskData taskData = (TaskData) taskList.get(0);
			List<TaskRecord> taskRecord = taskData.getTaskRecords();
			System.out.println(taskRecord.get(1).getRecordKey().getRowidXref());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void searchQuery(EjbSiperianClient client) {
		System.out.println("Inside the searchQuery method");
		SearchQueryRequest request = new SearchQueryRequest();
		request.setFilterCriteria("ROWID_XREF=541941");
		ArrayList<HubStateIndicator> recordState = new ArrayList<HubStateIndicator>();
		HubStateIndicator hsi = HubStateIndicator.ACTIVE;
		recordState.add(hsi);
		hsi = HubStateIndicator.PENDING;
		recordState.add(hsi);
		request.setRecordStates(recordState);
		System.out.println("HSI = "+recordState.get(0).getValue());
		System.out.println("HSI = "+recordState.get(1).getName());
		request.setSiperianObjectUid(SiperianObjectType.XREF.makeUid("C_PARTY"));
		SearchQueryResponse response = (SearchQueryResponse) client.process(request);
		List<Record> list = response.getRecords();
		Record record =  list.get(0);
		System.out.println("SQ XREF : "+record.getField("ROWID_XREF").getBigIntegerValue());
		System.out.println("SQ HSI : "+record.getField("HUB_STATE_IND").getBigIntegerValue());
	}

	private void restore(EjbSiperianClient client) {
		RestoreRequest request = new RestoreRequest();
		System.out.println("In the restore method");
		XrefKey key = new XrefKey();
		request.setSiperianObjectUid(SiperianObjectType.BASE_OBJECT.makeUid("C_PARTY"));
		key.setRowidXref("841947");
		List<XrefKey> list = new ArrayList<XrefKey>();
		list.add(key);
		request.setXrefKeys(list);
		RestoreResponse response = (RestoreResponse) client.process(request);
		System.out.println(response.getRestoreResults());
		List<XrefResult> responseList = response.getRestoreResults();
		XrefResult result = responseList.get(0);
		System.out.println(result.getMessage());
	}

}
