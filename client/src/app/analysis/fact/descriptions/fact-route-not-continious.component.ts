import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-not-continious",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.route-not-continious">
      The route is broken: the begin- and end-networknodes cannot be connected through ways either
      in the forward direction or the backward direction or both.
    </p>
  `
})
export class FactRouteNotContiniousComponent {
}
