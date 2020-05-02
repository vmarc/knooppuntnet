import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-not-backward",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-not-backward">
      There is no path in the backward direction (from end node to start node).
    </p>
  `
})
export class FactRouteNotBackwardComponent {
}
