package kpn.api.id

trait WithStringId extends Storable {
  def _id: String
}
