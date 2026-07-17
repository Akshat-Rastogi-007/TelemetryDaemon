package agent.platform.memory.impl;

import agent.platform.memory.MemoryProvider;
import agent.platform.memory.MemorySnapshot;
import com.sun.management.OperatingSystemMXBean;

public class MxBeanMemoryProvider implements MemoryProvider {


    private final OperatingSystemMXBean osBean;

    public MxBeanMemoryProvider(OperatingSystemMXBean operatingSystemMXBean) {
        this.osBean = operatingSystemMXBean;
    }

    @Override
    public MemorySnapshot snapshot() {

        long totalMemorySize = osBean.getTotalMemorySize();

        long freeMemorySize = osBean.getFreeMemorySize();

        long usedMemorySize = totalMemorySize - freeMemorySize;

        long totalSwapSpaceSize = osBean.getTotalSwapSpaceSize();

        long freeSwapSpaceSize = osBean.getFreeSwapSpaceSize();

        long usedSwapSpaceSize = totalSwapSpaceSize - freeSwapSpaceSize;

        return new MemorySnapshot(totalMemorySize,freeMemorySize,usedMemorySize,totalSwapSpaceSize,freeSwapSpaceSize,usedSwapSpaceSize);

    }
}
