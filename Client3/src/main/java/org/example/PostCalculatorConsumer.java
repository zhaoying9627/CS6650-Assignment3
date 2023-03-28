package org.example;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class PostCalculatorConsumer implements Runnable{
    private final BlockingQueue<Long> calculateBuffer;

    private int requestNum = 0;
    private long minLatency = Long.MAX_VALUE;

    private long maxLatency = Long.MIN_VALUE;

    private long latencySum = 0;

    public PostCalculatorConsumer(BlockingQueue<Long> calculateBuffer) {
        this.calculateBuffer = calculateBuffer;
    }

    public long getMinLatency() {
        return minLatency;
    }

    public long getMeanLatency() {
        return latencySum / requestNum;
    }

    public long getMaxLatency() {return maxLatency;}

    public void incRequestNum() {
        requestNum++;
    }

    public void updateLatencySum(long latency) {
        latencySum += latency;
    }

    public void run() {
        boolean active = true;
        try {
            while (active) {
                Long latency = calculateBuffer.take();
                // end condition
                if (latency < 0) {
                    active = false;
                } else {
                    // add number of requests
                    incRequestNum();
                    // update latency sum
                    updateLatencySum(latency);
                    // update min latency
                    minLatency = Math.min(minLatency, latency);
                    // update max latency
                    maxLatency = Math.max(maxLatency, latency);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws
            InterruptedException, IOException {
        // set number of requests to send
        int numOfRequests = 500000;
        // set number of producers
        int numOfProducer = 200;
        // set count down latch process
        CountDownLatch countDownLatchProcess = new CountDownLatch(numOfProducer);
        // set count down latch start
        CountDownLatch countDownLatchStart = new CountDownLatch(numOfProducer);
        // create a blocking queue for calculate
        BlockingQueue calculateBuffer = new LinkedBlockingQueue();
        // record start timestamp
        long startTime = System.currentTimeMillis();
        // create multiple producers
        for(int i = 0; i < numOfProducer; i++) {
            PostClientProducer producer = new PostClientProducer(calculateBuffer, countDownLatchProcess,
                    countDownLatchStart,
                    numOfRequests / numOfProducer);
            // start the producer
            new Thread(producer).start();
        }
        // create and start a calculate consumer
        PostCalculatorConsumer postCalculatorConsumer =  new PostCalculatorConsumer(calculateBuffer);
        new Thread(postCalculatorConsumer).start();
        // wait until all post requests are sent
        countDownLatchStart.await();
        // create and start a single thread get client
        GetClientProducer getClientProducer = new GetClientProducer(countDownLatchProcess);
        new Thread(getClientProducer).start();
        // wait until all post requests are processed
        countDownLatchProcess.await();
        //record end timestamp
        long endTime = System.currentTimeMillis();
        // send termination message to the calculate consumer
        calculateBuffer.put((long)(-1));
        // calculate post duration in seconds
        long postDuration = (endTime - startTime) / 1000;
        // calculate post throughput
        long postThroughput = numOfRequests / postDuration;
        // print out the post result
        System.out.println("The number of threads used to send requests is " + numOfProducer + ".");
        System.out.println("The number of POST requests is " + numOfRequests + ".");
        System.out.println("The total run (wall) time for POST is " + postDuration + " seconds.");
        System.out.println("The total throughput in POST requests per second is " + postThroughput + ".");
        System.out.println("The mean POST response time (millisecs) is " +
                postCalculatorConsumer.getMeanLatency() + ".");
        System.out.println("The max POST response time (millisecs) is " +
                postCalculatorConsumer.getMaxLatency() + ".");
        System.out.println("The min POST response time (millisecs) is " +
                postCalculatorConsumer.getMinLatency() + ".");
        System.out.println("The mean GET response time (millisecs) is " +
                getClientProducer.getMeanLatency() + ".");
        System.out.println("The max GET response time (millisecs) is " +
                getClientProducer.getMaxLatency() + ".");
        System.out.println("The min GET response time (millisecs) is " +
                getClientProducer.getMinLatency() + ".");
    }
}
