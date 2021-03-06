package gc_map;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GCMap<K,V> implements MapInterface.TimedSizableMap<K,V>{

    private final Map<K , TimedValue<V>> map;
    private Lock wLock;
    private Timer timer;

    public GCMap(long gcInterval, TimeUnit unit){
        this.timer = new Timer();
        this.wLock = new ReentrantLock();
        this.map = new HashMap<>();
        this.runGC(unit.toMillis(gcInterval));
    }

    @Override
    public void put(K key, V value, long duration, TimeUnit unit){
        wLock.lock();
        this.map.put(key,new TimedValue<V>(value, duration,unit));
        wLock.unlock();
    }

    @Override
    public Optional get(K key) {
        TimedValue val = this.map.get(key);
        if(!isExpired(val))
            return Optional.ofNullable(val.getValue());
        return Optional.ofNullable(null);
    }

    @Override
    public Optional remove(K key) {
        wLock.lock();
        TimedValue val = this.map.remove(key);
        if(!isExpired(val)) {
            wLock.unlock();
            return Optional.ofNullable(val.getValue());
        }
        return Optional.ofNullable(null);
    }

    @Override
    public long size() {
        wLock.lock();
        long size = this.map.values().stream().filter((vTimedValue)->{
             return !isExpired(vTimedValue);})
                .count();
        wLock.unlock();
        return size;
    }

    private boolean isExpired(TimedValue data) {
        if (data == null)
            return true;
        long now = System.currentTimeMillis();
        return data.getTime() <= now;
    }

    // This method will run in a background thread.
    // every inteval it will wake up and remove all expired objects to clear the memory
    private void runGC (long interval){
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wLock.lock();
                map.entrySet().removeIf(e-> {
                    return isExpired(e.getValue());
                });
                wLock.unlock();
            }
        },0,interval);
    }


    public void terminate(){
        this.timer.cancel();
    }

    public List<V> getAllValues(){
        List<V> result = new ArrayList<>();
        for(TimedValue<V> val: map.values()){
            result.add(val.getValue());
        }
        return result;
    }
}
