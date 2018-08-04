package actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final Integer magicNumber;

    static Props props(Integer magicNumber) {
        // You need to specify the actual type of the returned actor
        // since Java 8 lambdas have some runtime type information erased
        return Props.create(MyActor.class, () -> new MyActor(magicNumber));
    }

    public MyActor(Integer n){
        this.magicNumber = n;
    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(NuniActor.Greeting.class, g -> {
                    System.out.println("My actor I was greeted by " +  g.getGreeter());
                })
                .match(String.class, s -> {
                    log.info("Received String message: {}", s);
                    System.out.println("Porfavor got String : " + s);
                })
                .matchAny(o -> {
                    log.info("received unknown message");
                    System.out.println("Porfavor No String");
                })
                .build();
    }
}