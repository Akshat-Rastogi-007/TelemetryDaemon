package agent.collector;

public class CollectorRegister {

    private final Collector collector;

    private boolean enabled;

    public CollectorRegister(Collector collector, Boolean enabled) {
        this.collector = collector;
        this.enabled = enabled;
    }

    public Collector getCollector(){
        return collector;
    }

    public CollectorRegister(Collector collector) {
        this.collector = collector;
    }

    public Boolean isEnable(){
        return enabled;
    }

    public void disable(){
        enabled = Boolean.FALSE;
    }
    public void enable(){
        enabled = Boolean.TRUE;
    }

}
