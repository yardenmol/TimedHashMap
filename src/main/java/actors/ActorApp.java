package actors;


import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorApp {

    public static void main(String[] args){

        ActorSystem system = ActorSystem.create("PutaSystem");
        ActorRef isan = system.actorOf(MyActor.props(42), "isan");
        ActorRef yarden = system.actorOf(Props.create(NuniActor.class), "yarden");
        yarden.tell(new NuniActor.Greeting("from me isan rivka"), isan);
        system.terminate();

    }
}

