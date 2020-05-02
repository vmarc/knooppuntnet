import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-tag-missing",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-tag-missing">
      Routerelation does not contain the required _route_ tag.
    </markdown>
  `
})
export class FactRouteTagMissingComponent {
}
