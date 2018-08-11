package naive_benchmarks;

import actor_map.ActorTimedHaspMap;
import gc_map.GCMap;
import gc_map.MapInterface;
import stamped_item_map.StampedItemMap;

import java.util.concurrent.TimeUnit;

public class NaiveCompareBM {
    private static final long WARMUP_ITER = (long) 10_000;
    private static final long BENCHMARK_ITER = (long) 1_000_000;

    public static void main(String... args) {

//        System.gc();
//        new NaiveCompareBM().gcMapBMGet();
//        System.gc();
//        new NaiveCompareBM().actorMapBMGet();
//        System.gc();
//        new NaiveCompareBM().stampedMapBMGet();
//        System.gc();
//        new NaiveCompareBM().gcMapBMSetSizeGet();
//        System.gc();
//        new NaiveCompareBM().actorMapBMGet();
//        System.gc();
//        new NaiveCompareBM().stampedMapBMSetSizeGet();

    }

    public void gcMapBMSetSizeGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> gc = new GCMap<Integer, Integer>(10, TimeUnit.MILLISECONDS);

        //warmup
        long start = System.nanoTime();
        for (int i = 0; i < WARMUP_ITER; i++) {
            gc.put(i,i,10,TimeUnit.MILLISECONDS);
            gc.size();
            gc.get(i);
        }
        long end = System.nanoTime();
//        System.out.println("Warmup time: " + (end-start)/1E6 + "ms");

        System.gc();
        //benchmark
        long gcStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            gc.put(i,i,10,TimeUnit.MILLISECONDS);
            gc.size();
            gc.get(i);
        }
        long gcEnd = System.nanoTime();
        gc.terminate();
        System.out.println("gc: " + (gcEnd - gcStart)/1E6 + "ms");
    }

    public void actorMapBMSetSizeGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> actor = new ActorTimedHaspMap<>();

        //warmup
        long start = System.nanoTime();
        for (int i = 0; i < WARMUP_ITER; i++) {
            actor.put(i,i,10,TimeUnit.MILLISECONDS);
            actor.size();
            actor.get(i);
        }
        long end = System.nanoTime();
//        System.out.println("Warmup time: " + (end-start)/1E6 + "ms");

        System.gc();
        //benchmark
        long actorStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            actor.put(i,i,10,TimeUnit.MILLISECONDS);
            actor.size();
            actor.get(i);
        }
        long actorEnd = System.nanoTime();
        actor.terminate();
        System.out.println("actor: " + (actorEnd - actorStart)/1E6 + "ms");
    }

    public void stampedMapBMSetSizeGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> stamped = new ActorTimedHaspMap<>();

        //warmup
        long start = System.nanoTime();
        for (int i = 0; i < WARMUP_ITER; i++) {
            stamped.put(i,i,10,TimeUnit.MILLISECONDS);
            stamped.size();
            stamped.get(i);
        }
        long end = System.nanoTime();
//        System.out.println("Warmup time: " + (end-start)/1E6 + "ms");

        System.gc();
        //benchmark
        long stampedStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            stamped.put(i,i,10,TimeUnit.MILLISECONDS);
            stamped.size();
            stamped.get(i);
        }
        long stampedEnd = System.nanoTime();
        stamped.terminate();
        System.out.println("stamped: " + (stampedEnd - stampedStart)/1E6 + "ms");
    }

    public void gcMapBMGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> gc = new GCMap<Integer, Integer>(1, TimeUnit.SECONDS);

        //warmup
        for (int i = 0; i < WARMUP_ITER; i++) {
            gc.put(i,i,1,TimeUnit.SECONDS);
        }
        for (int i = 0; i < WARMUP_ITER; i++) {
            gc.get(i);
        }

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.gc();
        //benchmark
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            gc.put(i,i,1,TimeUnit.SECONDS);
        }
        long gcStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            gc.get(i);
        }
        long gcEnd = System.nanoTime();
        gc.terminate();
        System.out.println("gc: " + (gcEnd - gcStart)/1E6 + "ms");
    }

    public void actorMapBMGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> actor = new ActorTimedHaspMap<>();

        //warmup
        for (int i = 0; i < WARMUP_ITER; i++) {
            actor.put(i,i,1,TimeUnit.SECONDS);
        }
        for (int i = 0; i < WARMUP_ITER; i++) {
            actor.get(i);
        }

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.gc();
        //benchmark
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            actor.put(i,i,1,TimeUnit.SECONDS);
        }
        long actorStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            actor.get(i);
        }
        long actorEnd = System.nanoTime();
        actor.terminate();
        System.out.println("actor: " + (actorEnd - actorStart)/1E6 + "ms");
    }

    public void stampedMapBMGet(){
        final MapInterface.TimedSizableMap<Integer, Integer> stamped = new StampedItemMap<>();

        //warmup
        for (int i = 0; i < WARMUP_ITER; i++) {
            stamped.put(i,i,1,TimeUnit.SECONDS);
        }
        for (int i = 0; i < WARMUP_ITER; i++) {
            stamped.get(i);
        }

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.gc();
        //benchmark
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            stamped.put(i,i,1,TimeUnit.SECONDS);
        }
        long stampedStart = System.nanoTime();
        for (int i = 0; i < BENCHMARK_ITER; i++) {
            stamped.get(i);
        }
        long stampedEnd = System.nanoTime();
        stamped.terminate();
        System.out.println("stamped: " + (stampedEnd - stampedStart)/1E6 + "ms");
    }


}
