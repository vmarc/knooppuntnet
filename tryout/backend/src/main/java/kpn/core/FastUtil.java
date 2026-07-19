package kpn.core;

import it.unimi.dsi.fastutil.longs.LongSet;

/*
  https://github.com/vigna/fastutil/
  https://java-performance.info/hashmap-overview-jdk-fastutil-goldman-sachs-hppc-koloboke-trove-january-2015/
 */

public class FastUtil {
    public static boolean contains(LongSet set, long value) {
        return set.contains(value);
    }
}
