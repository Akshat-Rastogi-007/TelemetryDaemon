package agent.collector.manager;

import agent.collector.Collector;
import agent.collector.CollectorRegister;

import java.util.*;

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

    public void registerCollector(List<Collector> collectors){

        for ( Collector collector : collectors){
            registerCollector(collector);
        }

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
