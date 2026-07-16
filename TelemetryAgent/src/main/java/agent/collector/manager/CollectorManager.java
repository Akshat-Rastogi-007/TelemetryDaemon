package agent.collector.manager;

import agent.collector.Collector;
import agent.collector.CollectorRegister;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class CollectorManager {

    private final Map<String, CollectorRegister> collectorMap;

    public CollectorManager(Map<String, CollectorRegister> collectorMap) {
        this.collectorMap = collectorMap;
    }



    public void registerCollector(Collector collector){

        String name = collector.getId();
        CollectorRegister register = new CollectorRegister(collector,Boolean.TRUE);

        collectorMap.put(name,register);

    }

    public void removeCollector(String collectorName){

        collectorMap.remove(collectorName);

    }


    public Optional<CollectorRegister> getCollector(String name){

        CollectorRegister register = collectorMap.get(name);

        return Optional.ofNullable(register);

    }

    public Collection<CollectorRegister> getCollectors(){

        return Collections.unmodifiableCollection(
                collectorMap.values()
        );

    }

}
