package kpn.core.app

import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object ActorSystemConfig {

  def actorSystem(): ActorSystem = {
    ActorSystem("kpn", ConfigFactory.parseFile(new File("/kpn/conf/akka.conf")))
  }
}
