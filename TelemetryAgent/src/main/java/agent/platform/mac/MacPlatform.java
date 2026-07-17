package agent.platform.mac;

import agent.platform.Platform;
import agent.platform.cpu.CpuProvider;
import agent.platform.disk.DiskProvider;
import agent.platform.memory.MemoryProvider;
import agent.platform.network.NetworkProvider;

public class MacPlatform implements Platform {

    private final CpuProvider cpuProvider;
    private final MemoryProvider memoryProvider;
    private final DiskProvider diskProvider;

    public MacPlatform(CpuProvider cpuProvider, MemoryProvider memoryProvider, DiskProvider diskProvider) {
        this.cpuProvider = cpuProvider;
        this.memoryProvider = memoryProvider;
        this.diskProvider = diskProvider;
    }


    @Override
    public String getId() {
        return "mac";
    }

    @Override
    public CpuProvider cpu() {
        return cpuProvider;
    }

    @Override
    public MemoryProvider memory() {
        return memoryProvider;
    }

    @Override
    public DiskProvider disk() {
        return diskProvider;
    }

    @Override
    public NetworkProvider network() {
        return null;
    }
}
