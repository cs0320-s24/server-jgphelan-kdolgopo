package edu.brown.cs.student.main.DataAcess.CacheManagement;

import java.util.concurrent.TimeUnit;

public class CacheConfiguration {

  private final long maxSize;
  private final long expireAfterWriteDuration;
  private final TimeUnit timeUnit;

  public CacheConfiguration(long maxSize, long expireAfterWriteDuration, TimeUnit timeUnit) {
    this.maxSize = maxSize;
    this.expireAfterWriteDuration = expireAfterWriteDuration;
    this.timeUnit = timeUnit;
  }

  public long getMaxSize() {
    return maxSize;
  }

  public long getExpireAfterWriteDuration() {
    return expireAfterWriteDuration;
  }

  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

}