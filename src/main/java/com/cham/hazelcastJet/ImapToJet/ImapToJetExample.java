package com.cham.hazelcastJet.ImapToJet;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import jet.Employee;

import java.util.Map;

public class ImapToJetExample {

    public static void main(String[] args) {
        // Create the specification of the computation pipeline. Note
        // it's a pure POJO: no instance of Jet needed to create it.

        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        String MAP_NAME = "emp-map";
        String JET_MAP = "jet-emps";

        Pipeline p = Pipeline.create();

        p.drawFrom(Sources.remoteMap(MAP_NAME,clientConfig)).drainTo(Sinks.map(JET_MAP));

        // Start Jet, populate the input list
        JetInstance jet = Jet.newJetInstance();

        IMap<String, Employee> empMap = client.getMap(MAP_NAME);

        empMap.put("1", new Employee("Jim", 30, true, 234.45));
        empMap.put("2", new Employee("Tom", 55, true, 1234.00));
        empMap.put("3", new Employee("Tim", 38, false, 345.56));

            // Perform the computation
        jet.newJob(p).join();
            // Check the results

        Map<String, Long> counts = jet.getMap(JET_MAP);
        System.out.println(counts.size());

    }
}
