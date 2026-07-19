package kpn.api.common;

import kpn.api.common.Check;
import kpn.api.common.Fact;
import kpn.api.common.common.Ref;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record NetworkFact(
  Fact fact,
  Optional<String> elementType,
  ImmutableList<Long> elementIds,
  ImmutableList<Ref> elements,
  ImmutableList<Check> checks
) {
}

/*
package kpn.api.common

import kpn.api.common.common.Ref

case class NetworkFact(
  fact: Fact,
  elementType: Option[String] = None,
  // either 'elementIds' is filled in or 'elements', not both at the same time
  elementIds: Option[Seq[Long]] = None,
  elements: Option[Seq[Ref]] = None,
  checks: Option[Seq[Check]] = None
) {

  def size: Long = {
    elementIds match {
      case Some(ids) => ids.size
      case None =>
        elements match {
          case Some(es) => es.size
          case None =>
            checks match {
              case Some(c) => c.size
              case None => 0
            }
        }
    }
  }
}

*/
