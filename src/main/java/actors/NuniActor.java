package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class NuniActor extends AbstractActor {

    static public class Greeting {
        private final String from;

        public Greeting(String from) {
            this.from = from;
        }

        public String getGreeter() {
            return from;
        }
    }

    ActorRef isan = getContext().actorOf(MyActor.props(42), "isan");


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greeting.class, g -> {
                    sender().tell("i love u nuni from nuni",getSelf());
                    System.out.println("Nuni I was greeted by " +  g.getGreeter());
                })
                .matchAny(o -> {
                    System.out.println("Porfavor No String");
                })
                .build();
    }

}

