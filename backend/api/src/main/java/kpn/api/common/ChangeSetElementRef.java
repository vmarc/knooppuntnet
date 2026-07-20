package kpn.api.common;

public record ChangeSetElementRef(
  Long id,
  String name,
  Boolean happy,
  Boolean investigate
) {
}

/* TODO migrate

  def toRef: Ref = Ref(id, name)

*/
