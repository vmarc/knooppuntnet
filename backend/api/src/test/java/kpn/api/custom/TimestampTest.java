package kpn.api.custom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TimestampTest {

  @Test
  public void testLessThan() {

    BiConsumer<Timestamp, Timestamp> assertLessThan = (t1, t2) -> assertTrue(t1.lessThan(t2));

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2016, 5, 5, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 6, 5, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 6, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 6, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 6, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 6)
    );

    BiConsumer<Timestamp, Timestamp> assertNotLessThan = (t1, t2) -> assertFalse(t1.lessThan(t2));

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2014, 5, 5, 5, 5, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 4, 5, 5, 5, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 4, 5, 5, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 4, 5, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 4, 5)
    );

    assertNotLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 4)
    );
  }

  @Test
  public void testGreaterThan() {

    BiConsumer<Timestamp, Timestamp> assertGreaterThan = (t1, t2) -> assertTrue(t1.greaterThan(t2));

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2014, 5, 5, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 4, 5, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 4, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 4, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 4, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 4)
    );

    BiConsumer<Timestamp, Timestamp> assertNotGreaterThan = (t1, t2) -> assertFalse(t1.greaterThan(t2));

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2016, 5, 5, 5, 5, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 6, 5, 5, 5, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 6, 5, 5, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 6, 5, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 6, 5)
    );

    assertNotGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 6)
    );
  }

  @Test
  public void testCompare() {

    BiConsumer<Timestamp, Timestamp> assertLessThan = (t1, t2) -> assertTrue(t1.compareTo(t2) < 0);

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2016, 5, 5, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 6, 5, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 6, 5, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 6, 5, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 6, 5)
    );

    assertLessThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 6)
    );

    BiConsumer<Timestamp, Timestamp> assertCompareEqual = (t1, t2) -> assertTrue(t1.compareTo(t2) == 0);

    assertCompareEqual.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 5)
    );

    BiConsumer<Timestamp, Timestamp> assertGreaterThan = (t1, t2) -> assertTrue(t1.compareTo(t2) > 0);
    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2014, 5, 5, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 4, 5, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 4, 5, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 4, 5, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 4, 5)
    );

    assertGreaterThan.accept(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 4)
    );
  }

  @Test
  public void testEquality() {
    assertEquals(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 5, 5, 5, 5)
    );

    assertNotEquals(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2014, 5, 5, 5, 5, 5)
    );
    assertNotEquals(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 4, 5, 5, 5, 5)
    );
    assertNotEquals(
      new Timestamp(2015, 5, 5, 5, 5, 5),
      new Timestamp(2015, 5, 4, 5, 5, 5)
    );
  }

  @Test
  public void testOrdering() {
    List<Timestamp> timestamps = Arrays.asList(
      new Timestamp(2015, 1, 1, 1, 1, 1),
      new Timestamp(2015, 1, 1, 1, 1, 5),
      new Timestamp(2015, 1, 1, 1, 1, 4),
      new Timestamp(2015, 1, 1, 1, 1, 3)
    );

    List<Integer> orderedSeconds = timestamps.stream().sorted().map(Timestamp::second).collect(Collectors.toList());
    assertEquals(Arrays.asList(1, 3, 4, 5), orderedSeconds);
  }
}
