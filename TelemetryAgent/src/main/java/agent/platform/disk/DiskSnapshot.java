package agent.platform.disk;

public class DiskSnapshot {

    private final String name;

    private final long totalSpace;
    private final long freeSpace;
    private final long usableSpace;
    private final long usedSpace;

    public DiskSnapshot(String name, long totalSpace, long freeSpace, long usableSpace, long usedSpace) {
        this.name = name;
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.usableSpace = usableSpace;
        this.usedSpace = usedSpace;
    }

    public String getName() {
        return name;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public long getUsedSpace() {
        return usedSpace;
    }

    @Override
    public String toString() {
        return "DiskSnapshot{" +
                "name='" + name + '\'' +
                ", totalSpace=" + totalSpace +
                ", freeSpace=" + freeSpace +
                ", usableSpace=" + usableSpace +
                ", usedSpace=" + usedSpace +
                '}';
    }
}
