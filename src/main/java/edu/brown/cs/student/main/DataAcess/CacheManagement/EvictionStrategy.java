package edu.brown.cs.student.main.DataAcess.CacheManagement;

import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public abstract class EvictionStrategy<K, V> {

  /**
   * Configure the cache builder with the eviction strategy.
   *
   * @param cacheBuilder The cache builder to configure.
   */
  public abstract void configure(CacheBuilder<K, V> cacheBuilder);

  /**
   * A size-based eviction strategy.
   */
  public static class SizeBasedEvictionStrategy<K, V> extends EvictionStrategy<K, V> {
    private final int maxSize;

    public SizeBasedEvictionStrategy(int maxSize) {
      this.maxSize = maxSize;
    }

    @Override
    public void configure(CacheBuilder<K, V> cacheBuilder) {
      cacheBuilder.maximumSize(maxSize);
    }
  }

  /**
   * A time-based eviction strategy.
   */
  public static class TimeBasedEvictionStrategy<K, V> extends EvictionStrategy<K, V> {
    private final long duration;
    private final TimeUnit timeUnit;

    public TimeBasedEvictionStrategy(long duration, TimeUnit timeUnit) {
      this.duration = duration;
      this.timeUnit = timeUnit;
    }

    @Override
    public void configure(CacheBuilder<K, V> cacheBuilder) {
      cacheBuilder.expireAfterWrite(duration, timeUnit);
    }
  }

}