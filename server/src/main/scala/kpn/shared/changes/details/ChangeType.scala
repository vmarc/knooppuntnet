package kpn.shared.changes.details

object ChangeType {

  /*
    The change was created while processing the situation right after the redaction in 2012, and as such
    represents the oldest known situation of the element (if the element existed at that time).
   */
  val InitialValue = ChangeType("InitialValue")

  /*
    The element was added in this changeset. There is an 'after' situation but no 'before'.
   */
  val Create = ChangeType("Create")

  /*
    The element was updated in this changeset. There is both a 'before' and an 'after' situation.
   */
  val Update = ChangeType("Update")

  /*
    The element was deleted in this changeset. There is a 'before' situation but no 'after'. If the element
    looses the required tags to be a network or route relation or network node, then the ChangeType is 'Update'
    rather than 'Delete'.
   */
  val Delete = ChangeType("Delete")

}

case class ChangeType(name: String)
