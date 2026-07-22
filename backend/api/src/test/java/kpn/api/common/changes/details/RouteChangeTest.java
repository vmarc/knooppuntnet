package kpn.api.common.changes.details;

import kpn.api.common.Country;
import kpn.api.common.RouteType;
import kpn.api.common.data.Way;
import kpn.api.common.data.raw.RawNode;
import kpn.api.custom.Subset;
import kpn.core.test.ApiTestObjects;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

import static kpn.core.test.ApiTestObjects.newRouteData;
import static kpn.core.test.ApiTestObjects.newWay;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteChangeTest {

  @Test
  public void test() {

    Way way = newWay().build();
    IO.println(way);


    RawNode a = RawNode.builder()
      .id(123L)
//      .latitude("50.0")
//      .longitude("5.0")
      .version(1L)
      .changeSetId(1234567890L)
      .tags(ImmutableList.of())
      .build();

    IO.println(a);
  }

  @Test
  public void subsetsAreDerivedFromBothBeforeAndAfterSituation() {

    assertEquals(
      ImmutableList.of(Subset.beHiking, Subset.nlHiking),
      ApiTestObjects.newRouteChange()
        .before(
          Optional.of(
            newRouteData()
              .countries(ImmutableList.of(Country.NL))
              .routeTypes(ImmutableList.of(RouteType.HIKING))
              .build()
          )
        )
        .after(
          Optional.of(
            newRouteData()
              .countries(ImmutableList.of(Country.BE))
              .routeTypes(ImmutableList.of(RouteType.HIKING))
              .build()
          )
        )
        .build()
        .subsets()
    );

    //    newRouteChange(
    //      before = Some(newRouteData(countries = Seq.empty, routeTypes = Seq(RouteType.hiking))),
    //      after = Some(newRouteData(countries = Seq(Country.be), routeTypes = Seq(RouteType.hiking)))
    //    ).subsets should equal(Seq(Subset.beHiking))
    //
    //    newRouteChange(
    //      before = None,
    //      after = Some(newRouteData(countries = Seq(Country.be), routeTypes = Seq(RouteType.hiking)))
    //    ).subsets should equal(Seq(Subset.beHiking))
    //
    //    newRouteChange(
    //      before = Some(newRouteData(countries = Seq(Country.nl), routeTypes = Seq(RouteType.hiking))),
    //      after = None
    //    ).subsets should equal(Seq(Subset.nlHiking))
    //
    //    newRouteChange(
    //      before = None,
    //      after = None
    //    ).subsets shouldBe empty
  }
}
