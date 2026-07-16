package agent.platform;

import agent.platform.cpu.impl.MxBeanCpuProvider;
import agent.platform.linux.LinuxPlatform;
import agent.platform.mac.MacPlatform;
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

            return new LinuxPlatform(mxBeanCpuProvider);
        }

        if (os.contains("windows")) {
            return new WindowsPlatform();
        }

        if (os.contains("mac")) {
            return new MacPlatform();
        }

        throw new UnsupportedOperationException(
                "Unsupported operating system: " + System.getProperty("os.name"));
    }
}

