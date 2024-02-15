package edu.brown.cs.student.main.DataAcess;

import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public abstract class EvictionStrategy<K, V> {

  // Configure the cache builder with the eviction strategy.
  public abstract void configure(CacheBuilder<Object, Object> cacheBuilder);

  // A size-based eviction strategy.
  public static class SizeBasedEvictionStrategy<K, V> extends EvictionStrategy<K, V> {

    public SizeBasedEvictionStrategy() {
      // No maximum size parameter needed here
    }

    @Override
    public void configure(CacheBuilder<Object, Object> cacheBuilder) {
      // The maximum size configuration is removed from here
      // and should be set in the CacheConfiguration
    }
  }

  // A time-based eviction strategy.
  public static class TimeBasedEvictionStrategy<K, V> extends EvictionStrategy<K, V> {
    private final long duration;
    private final TimeUnit timeUnit;

    public TimeBasedEvictionStrategy(long duration, TimeUnit timeUnit) {
      this.duration = duration;
      this.timeUnit = timeUnit;
    }

    @Override
    public void configure(CacheBuilder<Object, Object> cacheBuilder) {
      cacheBuilder.expireAfterWrite(duration, timeUnit);
    }
  }
}
