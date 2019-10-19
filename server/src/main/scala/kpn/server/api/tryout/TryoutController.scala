package kpn.server.api.tryout

import kpn.core.db.NodeDoc
import kpn.core.db.couch.Database
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TryoutController(analysisDatabase: Database) {

  @GetMapping(value = Array("/tryout/node"))
  def node(): String = {
    val node = analysisDatabase.docWithId("node:1000007239", classOf[NodeDoc])
    s"<pre>${node.toString}</pre>"
  }

}
