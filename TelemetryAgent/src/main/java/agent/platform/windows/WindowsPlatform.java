package agent.platform.windows;

import agent.platform.Platform;
import agent.platform.cpu.CpuProvider;
import agent.platform.disk.DiskProvider;
import agent.platform.memory.MemoryProvider;
import agent.platform.network.NetworkProvider;

public class WindowsPlatform implements Platform {
    @Override
    public String getId() {
        return "windows";
    }

    @Override
    public CpuProvider cpu() {
        return null;
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
