package com.example.demo.config;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class DispatchTimer {

    public static final Map<Long, CompletableFuture<Void>> DISPATCH_FUTURES = new ConcurrentHashMap<>();

    public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(5);

    private DispatchTimer() {
    }
}