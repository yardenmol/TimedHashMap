package jmh_benchmark;

import actor_map.ActorTimedHaspMap;
import first_map.MapInterface;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ActorMapBM {

    @State(Scope.Thread)
    public static class MyState {
        public MapInterface.TimedSizableMap<Integer, String> map = new ActorTimedHaspMap<>();
        @Setup(Level.Trial)
        public void doSetup() {
            for(int i=0;i<1_000;++i){
                map.put(i, i + "", 1, TimeUnit.SECONDS);
            }
        }

        @TearDown(Level.Trial)
        public void doTearDown() {
            map.terminate();
            map =  new ActorTimedHaspMap<>();
        }

    }

    @Benchmark @BenchmarkMode({Mode.Throughput,Mode.SampleTime}) @OutputTimeUnit(TimeUnit.MINUTES)
    @Fork(value = 1)
    @Threads(value =1)
    @Warmup(iterations = 2)
    @Measurement(iterations = 3)
    public void putValue(MyState state) {
        state.map.put(10_000,10_000+"",1,TimeUnit.SECONDS);
    }
    public static void main(String ...args) throws IOException, RunnerException {
        org.openjdk.jmh.Main.main(args);
    }
}
