import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-orphan-route",
  template: `
    <p i18n="@@fact.description.orphan-route">
      This route does not belong to a network. The route was not added as a member to
      a valid network relation.
    </p>
  `
})
export class FactOrphanRouteComponent {
}
