// TODO migrate to Angular
package kpn.client.components.network

object NetworkNameCache {

  private val names = scala.collection.mutable.Map[Long, String]()

  def get(networkId: Long): Option[String] = {
    names.get(networkId)
  }

  def put(networkId: Long, name: String): Unit = {
    names.put(networkId, name)
  }
}
