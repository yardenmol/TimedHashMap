package jmh_benchmark;

import first_map.NaiveMap;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NaiveMapBM {

    final static int INSERT_AMOUNT = 10_000;
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void insert_1_milli_gc_1_milli_exp(){
        insert(1, INSERT_AMOUNT, 1);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void insert_1_milli_gc_1_sec_exp(){
        insert(1, INSERT_AMOUNT, 1000);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void insert_1_sec_gc_1_milli_exp(){
        insert(1000, INSERT_AMOUNT, 1);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void insert_1_sec_gc_1_sec_exp(){
        insert(1000, INSERT_AMOUNT, 1000);
    }

    public void insert(long interval, int amount, long expiration){

        NaiveMap<Integer, String> map = new NaiveMap<>(interval, TimeUnit.MILLISECONDS);
        for (int i=0;i<amount;++i){
            map.put(
                    i + new Random().nextInt(),
                    new Random().nextInt()+"",
                    expiration,
                    TimeUnit.MILLISECONDS);
        }
        map.terminate();
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void size_1_milli_gc_1_milli_exp(){
        size(1, INSERT_AMOUNT, 1);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void size_1_milli_gc_1_sec_exp(){
        size(1, INSERT_AMOUNT, 1000);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void size_1_sec_gc_1_mili_exp(){
        size(1000, INSERT_AMOUNT, 1);
    }
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 5)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void size_1_sec_gc_1_sec_exp(){
        size(1000, INSERT_AMOUNT, 1000);
    }
    public void size(long interval, int amount, long expiration){
        NaiveMap<Integer, String> map = new NaiveMap<>(interval, TimeUnit.MILLISECONDS);
        for (int i=0;i<amount;++i){
            map.put(
                    i + new Random().nextInt(),
                    new Random().nextInt()+"",
                    expiration,
                    TimeUnit.MILLISECONDS);
            map.size();
        }
        map.terminate();
    }

}
