import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-node-name-mismatch",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown i18n="@@fact.description.route-node-name-mismatch">
      The route name in the _note_ or _ref_ tag in the route relation does not match the expected name as
      derived from the start and end node of the route.
    </markdown>
  `
})
export class FactRouteNodeNameMismatchComponent {
}
