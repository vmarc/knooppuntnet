package kpn.api.common

enum ChangeType:
  /*
    The change represents the oldest known situation of the element (if the element existed at that time).
   */
  case InitialValue,

  /*
    The element was added in this changeset. There is an 'after' situation but no 'before'.
   */
  Create,

  /*
    The element was updated in this changeset. There is both a 'before' and an 'after' situation.
   */
  Update,

  /*
    The element was deleted in this changeset. There is a 'before' situation but no 'after'. If the element
    looses the required tags to be a network or route relation or network node, then the ChangeType is 'Update'
    rather than 'Delete'.
   */
  Delete