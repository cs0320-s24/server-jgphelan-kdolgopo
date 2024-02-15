package edu.brown.cs.student.main.DataAcess;

import java.util.concurrent.TimeUnit;

/**
 * The CacheConfiguration class is used to configure settings for a cache.
 * It allows specifying the maximum size of the cache, the duration after which
 * entries should expire, and the time unit for the expiration duration.
 */
public class CacheConfiguration {

  /**
   * The maximum size of the cache.
   */
  private final long maxSize;

  /**
   * The duration after which cache entries should expire after being written.
   */
  private final long expireAfterWriteDuration;

  /**
   * The time unit for the expiration duration.
   */
  private final TimeUnit timeUnit;

  /**
   * Constructs a new CacheConfiguration with specified settings.
   *
   * @param maxSize The maximum size of the cache.
   * @param expireAfterWriteDuration The duration after which cache entries
   *                                 should expire after being written.
   * @param timeUnit The time unit for the expiration duration.
   */
  public CacheConfiguration(long maxSize, long expireAfterWriteDuration, TimeUnit timeUnit) {
    this.maxSize = maxSize;
    this.expireAfterWriteDuration = expireAfterWriteDuration;
    this.timeUnit = timeUnit;
  }

  /**
   * Returns the maximum size of the cache.
   *
   * @return The maximum size.
   */
  public long getMaxSize() {
    return maxSize;
  }

  /**
   * Returns the duration after which cache entries should expire after being written.
   *
   * @return The expire-after-write duration.
   */
  public long getExpireAfterWriteDuration() {
    return expireAfterWriteDuration;
  }

  /**
   * Returns the time unit for the expiration duration.
   *
   * @return The time unit.
   */
  public TimeUnit getTimeUnit() {
    return timeUnit;
  }

}
