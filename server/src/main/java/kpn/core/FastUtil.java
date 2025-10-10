package kpn.core;

import it.unimi.dsi.fastutil.longs.LongSet;

public class FastUtil {
    public static boolean contains(LongSet set, long value) {
        return set.contains(value);
    }
}
