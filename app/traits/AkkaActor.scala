package traits

import play.api.libs.concurrent.Akka
import play.api.Play.current
import akka.actor.Props
import actors.IndexWriterActor
import actors.IndexSearcherActor

trait AkkaActor {
  
  // create one lucene actor
  val MESSAGE_TIMEOUT_IN_MILLIS = 5000
  val indexWriterActor = Akka.system.actorOf(Props[IndexWriterActor])
  val indexSearcherActor = Akka.system.actorOf(Props[IndexSearcherActor])

}