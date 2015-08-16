package hw5_final;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RoadManager {
    @SuppressWarnings("rawtypes")
    public BlockingQueue blockingQueue;
    public int numberBlocked;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public RoadManager(int capacity) {
        blockingQueue = new ArrayBlockingQueue(capacity);
        for (int i = 0; i < capacity; i++)
            blockingQueue.add(0);
        numberBlocked = 0;
    }

    public int size() {
        return blockingQueue.size();
    }

    public void take() throws InterruptedException {
        blockingQueue.take();
    }

    @SuppressWarnings("unchecked")
    public void give() throws InterruptedException {
        blockingQueue.put(0);
    }
}

