import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-name-missing",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-name-missing">
      The route relation does not have a _"note"_ or _"ref"_ tag with the route name.
    </markdown>
  `
})
export class FactRouteNameMissingComponent {
}
