package agent.platform.disk.impl;

import agent.platform.disk.DiskProvider;
import agent.platform.disk.DiskSnapshot;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileStoreDIskProvider implements DiskProvider {

    @Override
    public Collection<DiskSnapshot> snapshot() {


        System.out.println("*****Collecting Disk Data******");

        List<DiskSnapshot> snapshots = new ArrayList<>();

        for (FileStore fileStore : FileSystems.getDefault().getFileStores()) {

            try {

                System.out.println("Processing: " + fileStore.name());
                
                long totalSpace = fileStore.getTotalSpace();
                long usableSpace = fileStore.getUsableSpace();
                long freeSpace = fileStore.getUnallocatedSpace();
                long usedSpace = totalSpace - freeSpace;

                snapshots.add(
                        new DiskSnapshot(
                                fileStore.name(),
                                totalSpace,
                                freeSpace,
                                usableSpace,
                                usedSpace
                        )
                );
            } catch (IOException e) {
                System.err.println("Skipping FileStore: " + fileStore.name());
                continue;
            }
        }

        System.out.println("***************************************************************************8");
        System.out.println(snapshots);

        return snapshots;
    }

}