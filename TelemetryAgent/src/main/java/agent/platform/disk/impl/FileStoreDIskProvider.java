package agent.platform.disk.impl;

import agent.platform.disk.DiskProvider;
import agent.platform.disk.DiskSnapshot;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Path;

public class FileStoreDIskProvider implements DiskProvider {

    @Override
    public DiskSnapshot snapshot() {

        System.out.println("*****Collecting Disk Data******");

        try {

            Path root = Path.of("/");

            FileStore fileStore = java.nio.file.Files.getFileStore(root);

            System.out.println("Processing: " + fileStore.name());

            long totalSpace = fileStore.getTotalSpace();
            long usableSpace = fileStore.getUsableSpace();
            long freeSpace = fileStore.getUnallocatedSpace();
            long usedSpace = totalSpace - freeSpace;

            return new DiskSnapshot(
                    fileStore.name(),
                    totalSpace,
                    freeSpace,
                    usableSpace,
                    usedSpace
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to collect disk information for root filesystem",
                    e
            );
        }
    }
}