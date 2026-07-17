package agent.platform.memory;

public class MemorySnapshot {

    private final long totalMemory;
    private final long freeMemory;
    private final long usedMemory;
    private final long totalSwap;
    private final long freeSwap;
    private final long usedSwap;

    public MemorySnapshot(long totalMemory, long freeMemory, long usedMemory, long totalSwap, long freeSwap, long usedSwap) {
        this.totalMemory = totalMemory;
        this.freeMemory = freeMemory;
        this.usedMemory = usedMemory;
        this.totalSwap = totalSwap;
        this.freeSwap = freeSwap;
        this.usedSwap = usedSwap;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    public long getTotalSwap() {
        return totalSwap;
    }

    public long getFreeSwap() {
        return freeSwap;
    }

    public long getUsedSwap() {
        return usedSwap;
    }
}
