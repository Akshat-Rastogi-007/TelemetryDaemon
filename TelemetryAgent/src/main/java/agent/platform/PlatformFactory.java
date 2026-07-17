package agent.platform;

import agent.platform.cpu.impl.MxBeanCpuProvider;
import agent.platform.disk.impl.FileStoreDIskProvider;
import agent.platform.linux.LinuxPlatform;
import agent.platform.mac.MacPlatform;
import agent.platform.memory.impl.MxBeanMemoryProvider;
import agent.platform.windows.WindowsPlatform;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class PlatformFactory {

    public Platform create() {

        System.out.println("*****Configuring Platform*****");

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {

            OperatingSystemMXBean osBean =
                    (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            MxBeanCpuProvider mxBeanCpuProvider = new MxBeanCpuProvider(osBean);
            MxBeanMemoryProvider mxBeanMemoryProvider = new MxBeanMemoryProvider(osBean);
            FileStoreDIskProvider diskProvider = new FileStoreDIskProvider();

            return new LinuxPlatform(mxBeanCpuProvider, mxBeanMemoryProvider, diskProvider);
        }

        if (os.contains("windows")) {

            OperatingSystemMXBean osBean =
                    (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            MxBeanCpuProvider mxBeanCpuProvider = new MxBeanCpuProvider(osBean);
            MxBeanMemoryProvider mxBeanMemoryProvider = new MxBeanMemoryProvider(osBean);
            FileStoreDIskProvider diskProvider = new FileStoreDIskProvider();

            return new WindowsPlatform(mxBeanCpuProvider,mxBeanMemoryProvider,diskProvider);
        }

        if (os.contains("mac")) {

            OperatingSystemMXBean osBean =
                    (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            MxBeanCpuProvider mxBeanCpuProvider = new MxBeanCpuProvider(osBean);
            MxBeanMemoryProvider mxBeanMemoryProvider = new MxBeanMemoryProvider(osBean);
            FileStoreDIskProvider diskProvider = new FileStoreDIskProvider();

            return new MacPlatform(mxBeanCpuProvider,mxBeanMemoryProvider,diskProvider);
        }

        throw new UnsupportedOperationException(
                "Unsupported operating system: " + System.getProperty("os.name"));
    }
}

