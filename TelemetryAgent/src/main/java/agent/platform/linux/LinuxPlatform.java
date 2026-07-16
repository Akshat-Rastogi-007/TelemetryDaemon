package agent.platform.linux;

import agent.platform.Platform;
import agent.platform.cpu.CpuProvider;
import agent.platform.cpu.impl.MxBeanCpuProvider;
import agent.platform.disk.DiskProvider;
import agent.platform.memory.MemoryProvider;
import agent.platform.network.NetworkProvider;

public class LinuxPlatform implements Platform {

    private final CpuProvider cpuProvider;

    public LinuxPlatform(CpuProvider cpuProvider) {
        this.cpuProvider = cpuProvider;
    }

    @Override
    public String getId() {
        return "liunx";
    }

    @Override
    public CpuProvider cpu() {
        return cpuProvider;
    }

    @Override
    public MemoryProvider memory() {
        return null;
    }

    @Override
    public DiskProvider disk() {
        return null;
    }

    @Override
    public NetworkProvider network() {
        return null;
    }
}
