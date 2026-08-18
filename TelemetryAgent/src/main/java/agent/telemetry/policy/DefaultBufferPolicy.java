package agent.telemetry.policy;


public class DefaultBufferPolicy implements BufferPolicy {

    private final int maxSize;


    public DefaultBufferPolicy(int maxSize) {
        this.maxSize = maxSize;
    }


    @Override
    public boolean canStore(int currentSize) {
        return maxSize > currentSize;
    }
}
