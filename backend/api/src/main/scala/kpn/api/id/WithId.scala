package kpn.api.id

trait WithId extends Storable {
  def _id: Long
}
