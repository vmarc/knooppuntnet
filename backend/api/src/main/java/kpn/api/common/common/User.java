package kpn.api.common.common;

import kpn.core.doc.WithId;
import kpn.core.doc.WithStringId;

public record User(
  String _id
) implements WithStringId {
}
