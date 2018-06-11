package com.cham.hazelcastJet.service;

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
import domain.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Iterator;

@Service
public class HazelcastJetEmployeeService {

    private final ClientConfig clientConfig = new ClientConfig();
    private final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

    private final String MAP_NAME = "emp-map";
    private final  String JET_MAP = "jet-emps";

    public Flux<Employee> getStream(){

        Pipeline p = Pipeline.create();

        p.drawFrom(Sources.remoteMap(MAP_NAME,clientConfig))
                .filter((t) -> ((Employee)t.getValue()).getAge()>30)
                .filter((c) -> ((Employee)c.getValue()).getSalary() > 500)
                .drainTo(Sinks.map(JET_MAP));

        // Start Jet, populate the input list
        JetInstance jet = Jet.newJetInstance();

        IMap<String, Employee> empMap = client.getMap(MAP_NAME);

        empMap.put("2000", new Employee("Tom", 50, false, 1000.00, "us", "Doe"));
        empMap.put("3000", new Employee("Jim", 31, true, 3456.00, "us", "Doe"));
        empMap.put("4000", new Employee("Kal", 35, false, 23.90, "us", "Doe"));
        empMap.put("8000", new Employee("Toddy", 45, true, 234.00, "us", "Doe"));
        empMap.put("10000", new Employee("Timmy", 70, false, 12000.00, "us", "Doe"));

        // Perform the computation
        jet.newJob(p).join();

        // Check the results
        IMapJet<String, Employee> counts = jet.getMap(JET_MAP);

        Iterator itr = counts.values().iterator();

        while (itr.hasNext()){
            System.out.println(itr.next());
        }

        return Flux.fromStream(counts.values().stream());
    }
}
