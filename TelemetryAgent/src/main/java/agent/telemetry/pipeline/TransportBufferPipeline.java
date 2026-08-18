package agent.telemetry.pipeline;

import agent.telemetry.TelemetryBatch;
import agent.telemetry.buffer.TransportBuffer;
import agent.telemetry.policy.BufferPolicy;

public class TransportBufferPipeline {


    private final TransportBuffer transportBuffer;
    private final BufferPolicy bufferPolicy;

    public TransportBufferPipeline(TransportBuffer transportBuffer, BufferPolicy bufferPolicy) {
        this.transportBuffer = transportBuffer;
        this.bufferPolicy = bufferPolicy;
    }


    public void store(TelemetryBatch batch) {


        if(bufferPolicy.canStore(
                transportBuffer.getSize()
        )){


            transportBuffer.add(batch);

            System.out.println(
                    "____________Telemetry stored in buffer____________"
            );

            transportBuffer.printBuffer();


            return;
        }
        System.out.println("Buffer full. Notifying observers.");

        transportBuffer.notifyAllObservers();

        transportBuffer.add(batch);
        System.out.println("Telemetry stored in buffer after eviction.");


    }
}
