package actors

import models.tables._
import models.dtos._
import play.api.db.slick.Config.driver.simple.Session
import play.api.db.slick.DB
import play.api.test.WithApplication
import org.scalatest.concurrent._
import org.scalatest.matchers.ShouldMatchers
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef }
import scala.concurrent.duration._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.fixture.FlatSpec

class HelloAkkaSpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender
  with ShouldMatchers {

  def this() = this(ActorSystem("HelloAkkaSpec"))

//  override def afterAll: Unit = {
//    system.shutdown()
//    system.awaitTermination(10.seconds)
//  }

//  "An HelloAkkaActor" should "be able to set a new greeting" in {
//    val greeter = TestActorRef(Props[Greeter])
//    greeter ! WhoToGreet("testkit")
//    greeter.underlyingActor.asInstanceOf[Greeter].greeting should be("hello, testkit")
  //}

//  it should "be able to get a new greeting" in {
//    val greeter = system.actorOf(Props[Greeter], "greeter")
//    greeter ! WhoToGreet("testkit")
//    greeter ! Greet
//    expectMsgType[Greeting].message.toString should be("hello, testkit")
//  }
}
//}