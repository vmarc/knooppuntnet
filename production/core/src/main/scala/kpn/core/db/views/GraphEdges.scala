package kpn.core.db.views

/**
 * View to derive graph edges from routes (to be used for routing).
 *
 * Note: the second element in the key is a single digit pseudo random number
 * (derived from last character of the route id) that is included in the key
 * to be able to split up the large response to the query to pick up all graph
 * edges for a given network type, into 10 smaller responses. This is to overcome
 * the inability of Sohva to process chunked responses from couchdb at this
 * moment (https://github.com/gnieh/sohva/issues/34).
 */
object GraphEdges extends View {

  case class Key(networkType: String, prefix: Int, routeId: Long)
  case class Value(startNodeId: Long, endNodeId: Long, meters: Int)

//  override val map: String = """
//    function(doc) {
////      if(doc && doc.route) {
////        var key = {
////          networkType: doc.route.networkType,
////          prefix: parseInt(doc.route.id.slice(-1)),
////          routeId: new Number(doc.route.id)
////        };
////        var value = {
////          startNodeId: new Number(doc.route.analysis.startNodes[0].id),
////          endNodeId: new Number(doc.route.analysis.endNodes[0].id),
////          meters: parseInt(doc.route.meters)
////        };
////        emit(key, value);
////      }
//    }"""
//
//  override val reduce: Option[String] = None
}
