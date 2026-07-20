package kpn.api.custom;

import kpn.api.common.changes.ChangeAction;
import kpn.api.common.data.raw.RawElement;
import kpn.api.common.data.raw.RawNode;
import kpn.api.common.data.raw.RawRelation;
import kpn.api.common.data.raw.RawWay;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public record Change(
  ChangeAction action,
  ImmutableList<RawNode> nodes,
  ImmutableList<RawWay> ways,
  ImmutableList<RawRelation> relations
) {

  public ImmutableList<RawElement> elements() {
    return ImmutableList.copyOf(
      Iterables.concat(nodes(), ways(), relations())
    );
  }
}
