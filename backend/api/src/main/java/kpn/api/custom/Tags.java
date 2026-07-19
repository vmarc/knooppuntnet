package kpn.api.custom;

import kpn.api.custom.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Tags {

  private static final String VALUE_SEPARATOR = ";";

  private Tags() {
  }

  @SafeVarargs
  public static List<Tag> from(Map.Entry<String, String>... tags) {
    return Arrays.stream(tags)
      .map(entry -> new Tag(entry.getKey(), entry.getValue()))
      .toList();
  }

  public static List<Tag> from(Map<String, String> map) {
    return map.entrySet().stream()
      .map(entry -> new Tag(entry.getKey(), entry.getValue()))
      .toList();
  }

  public static String toString(List<Tag> tags) {
    return tags.stream()
      .map(tag -> tag.key() + "=" + tag.value())
      .collect(Collectors.joining(", "));
  }

  public static Optional<String> get(List<Tag> tags, String key) {
    return tags.stream()
      .filter(tag -> tag.key().equals(key))
      .map(Tag::value)
      .findFirst();
  }

  public static List<String> values(List<Tag> tags, String key) {
    return get(tags, key)
      .filter(value -> !value.isEmpty())
      .map(kpn.api.custom.Tags::splitAndNormalize)
      .orElse(List.of());
  }

  public static boolean has(List<Tag> tags, String key, String... allowedValues) {
    return tags.stream().anyMatch(tag ->
      tag.key().equals(key) && (
        allowedValues.length == 0 ||
          splitAndNormalize(tag.value()).stream()
            .anyMatch(value -> Arrays.asList(allowedValues).contains(value))
      )
    );
  }

  public static List<String> splitAndNormalize(String value) {
    if (value.contains(VALUE_SEPARATOR)) {
      return Arrays.stream(value.split(VALUE_SEPARATOR))
        .map(String::trim)
        .filter(part -> !part.isEmpty())
        .sorted()
        .toList();
    } else {
      return List.of(value.trim());
    }
  }
}
