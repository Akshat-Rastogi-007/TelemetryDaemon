package agent.platform.disk;

import java.util.Collection;

public interface DiskProvider {

    Collection<DiskSnapshot> snapshot();

}
