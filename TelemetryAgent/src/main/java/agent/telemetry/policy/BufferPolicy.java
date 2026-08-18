package agent.telemetry.policy;

public interface BufferPolicy {


    boolean canStore(
            int currentSize
    );

}