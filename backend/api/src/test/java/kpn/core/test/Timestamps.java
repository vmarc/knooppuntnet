package kpn.core.test;

import kpn.api.custom.Timestamp;

public class Timestamps {

  public static final Timestamp defaultTimestamp = new Timestamp(2015, 8, 11, 0, 0, 0);
  public static final Timestamp before           = new Timestamp(2015, 8, 11, 0, 0, 1);
  public static final Timestamp from             = new Timestamp(2015, 8, 11, 0, 0, 2);
  public static final Timestamp until            = new Timestamp(2015, 8, 11, 0, 0, 3);
  public static final Timestamp after            = new Timestamp(2015, 8, 11, 0, 0, 4);
}
