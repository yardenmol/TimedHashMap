package naive_benchmarks;

import actor_map.ActorTimedHaspMap;
import gc_map.MapInterface;
import gc_map.GCMap;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GCMapCompareSizeCall {

    private final MapInterface.TimedSizableMap<Integer, Integer> naive = new GCMap<Integer, Integer>(10, TimeUnit.MILLISECONDS);
    private final MapInterface.TimedSizableMap<Integer, Integer> actor = new ActorTimedHaspMap<>();

    private static final long WARMUP_ITER = (long) 5_000;
    private static final long BENCHMARK_ITER = (long) 5_000;


    public static void main(String... args) {
            new GCMapCompareSizeCall().measureSizeCall();
    }

    // test with expiration = , gcInterval = ,
    public void measureSizeCall(){
        //warm-up
        sampleSizeTime(WARMUP_ITER, 1,TimeUnit.MILLISECONDS, 1);
        System.gc();

        //measure
        ArrayList<Long> MilliGCMilliExp = sampleSizeTime(BENCHMARK_ITER, 1,TimeUnit.MILLISECONDS, 1);
        System.gc();
        ArrayList<Long> MilliGCSecExp = sampleSizeTime(BENCHMARK_ITER, 1,TimeUnit.SECONDS, 1);
        System.gc();
        ArrayList<Long> SecGCMilliExp = sampleSizeTime(BENCHMARK_ITER, 1,TimeUnit.MILLISECONDS, 1000);
        System.gc();
        ArrayList<Long> SecGCSecExp = sampleSizeTime(BENCHMARK_ITER, 1,TimeUnit.SECONDS, 1000);

        //average results
        System.out.println("MilliGCMilliExp: "+ getAverage(MilliGCMilliExp));
        System.out.println("MilliGCSecExp: "+ getAverage(MilliGCSecExp));
        System.out.println("SecGCMilliExp: "+ getAverage(SecGCMilliExp));
        System.out.println("SecGCSecExp: "+ getAverage(SecGCSecExp));
    }

    public long getAverage(ArrayList<Long> arr){
        return arr.stream().reduce((a, b)->{
            return a+b;
        }).get()/BENCHMARK_ITER;
    }

    // test the differences of getting size between different GC intervals
    public ArrayList<Long> sampleSizeTime(long loops, long expDuration ,TimeUnit expTimeUnit, long gcInterval){
        ArrayList<Long> stamps = new ArrayList<>();
        GCMap<Integer, String> map = new GCMap<>(gcInterval, TimeUnit.MILLISECONDS);
        for (int i = 0; i < loops; ++i ){
            map.put(i,i+"",expDuration,expTimeUnit);
            long start = System.nanoTime();
            map.size();
            long end = System.nanoTime();
            stamps.add(end - start);
        }
        return stamps;
    }


}
