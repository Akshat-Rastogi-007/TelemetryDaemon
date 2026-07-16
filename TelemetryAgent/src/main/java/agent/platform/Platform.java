package agent.platform;

import agent.platform.cpu.CpuProvider;
import agent.platform.disk.DiskProvider;
import agent.platform.memory.MemoryProvider;
import agent.platform.network.NetworkProvider;

public interface Platform {

    String getId();
    CpuProvider cpu();
    MemoryProvider memory();
    DiskProvider disk();
    NetworkProvider network();


}
