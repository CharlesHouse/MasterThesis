package com.myapp.integration;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

public class Metrics {

    public static final Summary requestLatency = Summary.build()
      .name("requests_latency_seconds")
      .help("Request latency in seconds.").register();

    public static final Counter requestFailures = Counter.build()
      .name("requests_failures_total")
      .help("Request failures.").register();

    public static final Counter requestsTotal = Counter.build()
      .name("requests_total")
      .help("Total number of HTTP requests made.").register();


}