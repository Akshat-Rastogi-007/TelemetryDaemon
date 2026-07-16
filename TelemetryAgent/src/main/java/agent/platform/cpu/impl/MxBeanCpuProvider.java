package agent.platform.cpu.impl;

import agent.platform.cpu.CpuProvider;
import agent.platform.cpu.CpuSnapshot;
import com.sun.management.OperatingSystemMXBean;

public class MxBeanCpuProvider implements CpuProvider {

    private final OperatingSystemMXBean osBean;

    public MxBeanCpuProvider(OperatingSystemMXBean operatingSystemMXBean) {
        this.osBean = operatingSystemMXBean;
    }

    @Override
    public CpuSnapshot snapshot() {

        System.out.println("*****Generating Snapshot*****");


        double cpuLoad = osBean.getCpuLoad();

        int availableProcessors = osBean.getAvailableProcessors();

        double systemLoadAverage = osBean.getSystemLoadAverage();


        CpuSnapshot cpuSnapshot = new CpuSnapshot(cpuLoad, availableProcessors, systemLoadAverage);

        System.out.printf("*****Snapshot -> %s***** \n",cpuSnapshot);

        return cpuSnapshot;

    }
}
