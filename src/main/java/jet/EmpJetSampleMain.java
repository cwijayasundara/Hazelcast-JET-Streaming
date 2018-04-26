package jet;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;

import java.util.Map;

import static com.hazelcast.jet.pipeline.JournalInitialPosition.START_FROM_CURRENT;

public class EmpJetSampleMain {

    public static void main(String[] args) {

        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        String MAP_NAME = "employee-map";

        IMap<String, Employee> empMap = client.getMap(MAP_NAME);

        empMap.set("Alice", new Employee("Alice", 35, true, 500.50));
        empMap.set("Bob", new Employee("Bob", 54, false, 1000.45));
        empMap.set("Sam", new Employee("Sam", 20, true, 100.45));
        empMap.set("Tom", new Employee("Tom", 21, false, 234.56));
        empMap.set("Antoney", new Employee("Antoney", 60, true, 23.45));

        JetInstance jetInstance = Jet.newJetInstance();

        Pipeline pipeline = Pipeline.create();
        //Let’s use the default client config. You could also configure IPs of your remote IMDG cluster

        pipeline.drawFrom(Sources.remoteMapJournal(MAP_NAME, clientConfig, START_FROM_CURRENT))
                .drainTo(Sinks.map("counts"));

        jetInstance.newJob(pipeline).join();

        Map<String, Employee> employeeList= jetInstance.getMap("counts");
        System.out.println(employeeList.size());

    }
}
