package agent.collector;

public class CpuCollector implements Collector{


    @Override
    public String getId() {
        return "cpu";
    }

    @Override
    public void collect() {
        System.out.println("Collecting the cpu metric");
    }
}
