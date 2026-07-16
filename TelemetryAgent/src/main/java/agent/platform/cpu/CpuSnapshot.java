package agent.platform.cpu;

public class CpuSnapshot {

    private final double cpuUsage;
    private final int logicalProcessors;
    private final double loadAverage;

    public CpuSnapshot(double cpuUsage, int logicalProcessors, double loadAverage) {
        this.cpuUsage = cpuUsage;
        this.logicalProcessors = logicalProcessors;
        this.loadAverage = loadAverage;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public int getLogicalProcessors() {
        return logicalProcessors;
    }

    public double getLoadAverage() {
        return loadAverage;
    }

    @Override
    public String toString() {
        return "CpuSnapshot{" +
                "cpuUsage=" + cpuUsage +
                ", logicalProcessors=" + logicalProcessors +
                ", loadAverage=" + loadAverage +
                '}';
    }
}
