package kpn.api.common.status;

import kpn.api.common.status.NameValue;

import com.google.common.collect.ImmutableList;

public record BarChart2dValue(
  String name,
  ImmutableList<NameValue> series
) {
}

/*
package kpn.api.common.status

case class BarChart2dValue(
  name: String,
  series: Seq[NameValue]
)

*/
