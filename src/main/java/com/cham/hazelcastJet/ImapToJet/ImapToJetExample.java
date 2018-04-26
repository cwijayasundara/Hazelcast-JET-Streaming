package com.cham.hazelcastJet.ImapToJet;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.IMapJet;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import jet.Employee;

import java.util.Iterator;

public class ImapToJetExample {

    public static void main(String[] args) {
        // Create the specification of the computation pipeline. Note
        // it's a pure POJO: no instance of Jet needed to create it.

        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        String MAP_NAME = "emp-map";
        String JET_MAP = "jet-emps";

        Pipeline p = Pipeline.create();

        p.drawFrom(Sources.remoteMap(MAP_NAME,clientConfig))
                .filter((e) -> e.getKey().equals("78"))
                .filter((t) -> ((Employee)t.getValue()).getAge()>30)
                .drainTo(Sinks.map(JET_MAP));

        // Start Jet, populate the input list
        JetInstance jet = Jet.newJetInstance();

        IMap<String, Employee> empMap = client.getMap(MAP_NAME);

        for (int count =1;count<100;count++) {
            Employee employee = new Employee("Jimbo", 34, true, 500.00);
            empMap.put(Integer.toString(count), employee);
        }

            // Perform the computation
        jet.newJob(p).join();

            // Check the results
        IMapJet<String, Long> counts = jet.getMap(JET_MAP);
        Iterator itr = counts.values().iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }
    }
}
