import {Component, Input} from "@angular/core";
import {RouteInfo} from "../../../../kpn/shared/route/route-info";
import {Util} from "../../../../components/shared/util";
import {I18nService} from "../../../../i18n/i18n.service";

@Component({
  selector: "kpn-route-summary",
  template: `
    <div>

      <p *ngIf="!route.ignored">
        {{route.summary.meters}} m
      </p>

      <p>
        <osm-link-relation relationId="route.id"></osm-link-relation>
        (
        <josm-relation relationId="route.id"></josm-relation>
        )
      </p>

      <kpn-network-type [networkType]="route.summary.networkType"></kpn-network-type>

      <p *ngIf="route.summary.country">
        <kpn-country-name [country]="route.summary.country"></kpn-country-name>
      </p>

      <p *ngIf="!route.ignored && isRouteBroken()">
        TODO <!-- UiImage("warning.png") -->
        <span>This route seems broken.</span> <!-- Er lijkt iets mis met deze route. -->
        )
      </p>

      <p *ngIf="!route.ignored && isRouteIncomplete()">

        TODO <!-- UiImage("warning.png")-->

        Route definition is incomplete (has tag _"fixme=incomplete"_).
        <!--De route definitie is onvolledig (heeft tag _"fixme=incomplete"_).-->
      </p>


      <p *ngIf="route.ignored">
        This route is not included in the analysis.
        <!-- Deze route is niet opgenomen in de analyse. -->
      </p>

      <p *ngIf="!route.ignored && !route.active" class="warning">
        This route is not active anymore.
        <!-- Deze route is niet actief meer. -->
      </p>

    </div>
  `
})
export class RouteSummaryComponent {

  @Input() route: RouteInfo;

  constructor(private i18nService: I18nService) {
  }

  isRouteBroken() {
    // TODO this.route.facts.contains(Fact.RouteBroken))
    return true;
  }

  isRouteIncomplete() {
    // TODO this.route.facts.contains(Fact.RouteIncomplete)
    return true;
  }

  countryName(): string {
    return this.i18nService.name("country." + Util.safeGet(() => this.route.summary.country.domain));
  }
}
