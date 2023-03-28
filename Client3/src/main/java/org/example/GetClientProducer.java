package org.example;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.MatchesApi;
import io.swagger.client.api.StatsApi;
import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.MatchStats;
import io.swagger.client.model.Matches;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GetClientProducer implements Runnable{
    private CountDownLatch countDownLatchProcess;

    private int requestNum = 0;

    private long minLatency = Long.MAX_VALUE;

    private long maxLatency = Long.MIN_VALUE;

    private long latencySum = 0;

    private final String BASE_PATH = "http://ServerLoadBalancer-368088754.us-west-2.elb.amazonaws.com:8080/Sever3_war";

    private final int SWPIERBOUND = 5000;

    public GetClientProducer (CountDownLatch countDownLatchProcess) {
        this.countDownLatchProcess = countDownLatchProcess;
    }

    public int getRequestNum() {
        return requestNum;
    }

    public long getMinLatency() {
        return minLatency;
    }

    public long getMaxLatency() {
        return maxLatency;
    }

    public long getLatencySum() {
        return latencySum;
    }

    public long getMeanLatency() {
        return latencySum / requestNum;
    }

    public void incRequestNum() {
        requestNum++;
    }

    public void updateLatencySum(long latency) {
        latencySum += latency;
    }

    public void run() {
        while (countDownLatchProcess.getCount() > 0) {
            ApiClient apiClient = new ApiClient();
            apiClient.setBasePath(BASE_PATH);
            Random rand = new Random();
            int flag = rand.nextInt(2);
            String userID = String.valueOf(rand.nextInt(SWPIERBOUND) + 1);
            long startTimestamp = System.currentTimeMillis();
            // initialize end timestamp
            long endTimestamp = 0;
            if (flag == 0) {
                // use matches api
                MatchesApi matchesApi = new MatchesApi(apiClient);
                try {
                    ApiResponse<Matches> response = matchesApi.matchesWithHttpInfo(userID);
                    endTimestamp = System.currentTimeMillis();
                } catch (ApiException e) {
                    endTimestamp = System.currentTimeMillis();
                }
            } else {
                // use stats api
                StatsApi statsApi = new StatsApi(apiClient);
                try {
                    ApiResponse<MatchStats> response = statsApi.matchStatsWithHttpInfo(userID);
                    endTimestamp = System.currentTimeMillis();
                } catch (ApiException e) {
                    endTimestamp = System.currentTimeMillis();
                }
            }
            // calculate latency
            long latency = endTimestamp - startTimestamp;
            // add number of requests
            incRequestNum();
            // update latency sum
            updateLatencySum(latency);
            // update min latency
            minLatency = Math.min(minLatency, latency);
            // update max latency
            maxLatency = Math.max(maxLatency, latency);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
