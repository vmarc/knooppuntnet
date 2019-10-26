package kpn.server.api.tryout

import kpn.core.database.Database
import kpn.core.database.doc.NodeDoc
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
