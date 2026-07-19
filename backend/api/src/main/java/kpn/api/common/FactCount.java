package kpn.api.common;

import kpn.api.common.Fact;

public record FactCount(
  Fact fact,
  Long count
) {
}

/*
package kpn.api.common

case class FactCount(fact: Fact, count: Long)

*/
