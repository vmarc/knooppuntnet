package kpn.api.custom;

import kpn.api.common.data.Tagable;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagsTest {

  private record TestObject(
    ImmutableList<Tag> tags
  ) implements Tagable {}

  @Test
  public void hasTag_falseWhenTagIsMissing() {
    TestObject taggable = new TestObject(ImmutableList.of());
    assertFalse(taggable.hasTag("key"));
  }

  @Test
  public void hasTag_tagWithSingleValue() {
    TestObject taggable = taggableWithTagValue("value");
    assertTrue(taggable.hasTag("key"));
    assertTrue(taggable.hasTag("key", "value"));
    assertFalse(taggable.hasTag("key", "bla"));
  }

  @Test
  public void hasTag_tagWithMultipleValues() {
    TestObject taggable = taggableWithTagValue("value1;value2");
    assertTrue(taggable.hasTag("key", "value1"));
    assertTrue(taggable.hasTag("key", "value2"));
    assertFalse(taggable.hasTag("key", "bla"));
  }

  @Test
  public void tagValues_noValuesWhenTagIsMissing() {
    TestObject taggable = new TestObject(ImmutableList.of());
    assertEquals(taggable.tagValues("key"), ImmutableList.of());
  }

  @Test
  public void values_noValuesWhenTagValueIsEmpty() {
    assertEquals(taggableWithTagValue("").tagValues("key"), ImmutableList.of());
  }

  @Test
  public void tagValues() {
    assertEquals(taggableWithTagValue("value").tagValues("key"), ImmutableList.of("value"));
    assertEquals(taggableWithTagValue("  value  ").tagValues("key"), ImmutableList.of("value"));
    assertEquals(taggableWithTagValue("value1;value2").tagValues("key"), ImmutableList.of("value1", "value2"));
    assertEquals(taggableWithTagValue("value1;;value2").tagValues("key"), ImmutableList.of("value1", "value2"));
    assertEquals(taggableWithTagValue("value1  ;  ;  value2").tagValues("key"), ImmutableList.of("value1", "value2"));
  }

  private static TestObject taggableWithTagValue(String value) {
    return new TestObject(
      ImmutableList.of(
        new Tag("key", value)
      )
    );
  }
}
