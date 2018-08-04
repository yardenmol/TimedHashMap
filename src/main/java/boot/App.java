package boot;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
//        Options opt = new OptionsBuilder()
//                .include(NaiveMapBM.class.getSimpleName())
//                .warmupIterations(1)
//                .measurementIterations(1)
//                .forks(1)
//                .build();
//        new Runner(opt).run();
    }
}
