package kpn.api.common.common;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import com.google.common.collect.ImmutableSet;

public record ReferencedElements(
  ImmutableSet<Long> nodeIds,
  ImmutableSet<Long> routeIds
) {

  public static ReferencedElements merge(List<ReferencedElements> elements) {
    return new ReferencedElements(
      mergeField(elements, ReferencedElements::nodeIds),
      mergeField(elements, ReferencedElements::routeIds)
    );
  }

  private static ImmutableSet<Long> mergeField(
    List<ReferencedElements> elements,
    Function<ReferencedElements, ImmutableSet<Long>> fieldExtractor
  ) {
    return elements.stream()
      .flatMap(element -> fieldExtractor.apply(element).stream())
      .collect(ImmutableSet.toImmutableSet());
  }
}
