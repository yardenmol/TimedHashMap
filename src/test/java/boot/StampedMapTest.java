package boot;


import gc_map.GCMap;
import gc_map.MapInterface;
import org.junit.Test;
import stamped_item_map.StampedItemMap;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class StampedMapTest {

    @Test
    public void shouldInsertValue(){
        MapInterface.TimedSizableMap<String, Integer> map = new StampedItemMap<>();
        assertEquals(0,map.size());
        map.put("yarden",5,4,TimeUnit.SECONDS);
        assertEquals(1,map.size());
        map.terminate();
    }

    @Test
    public void shouldGcRemove() throws InterruptedException {
        MapInterface.TimedSizableMap<String, Integer> map = new StampedItemMap<>();
        map.put("yarden",5,1,TimeUnit.SECONDS);
        Thread.sleep(2000);
//        assertNull(map.get("yarden"));
//        assertFalse(map.get("yarden").isPresent());
        assertEquals(0,map.size());
        map.terminate();
    }

    @Test
    public void shouldReturnNull() throws InterruptedException {
        MapInterface.TimedSizableMap<String, Integer> map = new StampedItemMap<>();
        map.put("yarden",5,1,TimeUnit.SECONDS);
        Thread.sleep(1100);
//        assertFalse(map.get("yarden").isPresent());
        assertEquals(0,map.size());
        map.terminate();
    }



    @Test
    public void shouldInsertParallel() throws InterruptedException {
        for (int j=0;j<15;j++){
            MapInterface.TimedSizableMap<Integer, Integer> map = new StampedItemMap<>();

            Thread t1 = new Thread(()->{
                for(int i=0; i<100;i++)
                    map.put(i,i,2,TimeUnit.SECONDS);
            });

            Thread t2 = new Thread(()->{
                for(int i=100; i<200;i++)
                    map.put(i,i,2,TimeUnit.SECONDS);
            });
            t1.start();
            t2.start();

            t1.join();
            t2.join();

            assertEquals(200,map.size());
            Thread.sleep(2500);
            assertEquals(0,map.size());
            map.terminate();
        }

    }

    @Test
    public void shouldReturnLastValueOfKey() throws InterruptedException {
        MapInterface.TimedSizableMap<String, Integer> map = new StampedItemMap<>();
        map.put("isan",5,10,TimeUnit.SECONDS);
        map.put("isan",6,10,TimeUnit.SECONDS);
        map.get("isan").ifPresent(val->{
            assertEquals(6, (int)val);
        });
        map.terminate();
    }
    @Test
    public void shouldDropHalfInputs() throws InterruptedException {
        MapInterface.TimedSizableMap<Integer, Integer> map = new StampedItemMap<>();
        int size = 10_000;
        Thread t1 = new Thread(()->{
            for(int i=0;i<size;++i){
                map.put(i,i,10,TimeUnit.MILLISECONDS);
            }
        });
        Thread t2 = new Thread(()->{
            for(int i=0;i<size;++i){
                map.put(i+size,i,50,TimeUnit.SECONDS);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        Thread.sleep(2000);
        assertEquals(size,map.size());
        map.terminate();
    }
}
