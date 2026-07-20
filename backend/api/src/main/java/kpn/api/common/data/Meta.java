package kpn.api.common.data;

import kpn.api.custom.Timestamp;

public interface Meta {

  public abstract Long version();

  public abstract Long changeSetId();

  public abstract Timestamp timestamp();
}
