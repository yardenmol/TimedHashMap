package stamped_item_map;

import actor_map.StampedItem;
import akka.actor.ActorRef;
import gc_map.MapInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StampedItemMap<K,V> implements MapInterface.TimedSizableMap<K,V> {

    private final Map<K, StampedItem<V>> innerMap = new HashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
    private final AtomicLong stateMarker = new AtomicLong(1);
    private Lock lock = new ReentrantLock();

    @Override
    public void put(K key, V value, long duration, TimeUnit unit) {
        lock.lock();
        long stamp = stateMarker.getAndIncrement();
        innerMap.put(key, new StampedItem<>(value,stamp));
        lock.unlock();
        cleaner.schedule(() -> tryRemove(key, stamp), duration, unit);
    }

    private void tryRemove(K key, long mark) {
        lock.lock();
        if (markEquals(key, mark)) {
            innerMap.remove(key);
        }
        lock.unlock();
    }

    private boolean markEquals(K key, long stamp) {
        if (innerMap.get(key) != null) {
            return innerMap.get(key).stamp == stamp;
        } else {
            return false;
        }

//        return Optional.
//                ofNullable(innerMap.get(key)).
//                map(stamped -> stamped.stamp == stamp).
//                orElse(false);

    }
    @Override
    public Optional<V> get(K key) {
        StampedItem<V> stampedItem = innerMap.get(key);
        if(stampedItem!=null)
            return Optional.ofNullable(stampedItem.item);
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<V> remove(K key) {
        return Optional.ofNullable(innerMap.remove(key)).map(so -> so.item);
    }

    @Override
    public void terminate() {
        cleaner.shutdownNow();
    }

    @Override
    public long size() {
        return innerMap.size();
    }
}
