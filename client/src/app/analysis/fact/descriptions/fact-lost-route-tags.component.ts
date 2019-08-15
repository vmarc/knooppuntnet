import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-lost-route-tags",
  template: `
    <p i18n="@@fact.description.lost-route-tags">
      This relation is no longer a valid network route because a required tag has been removed.
    </p>
  `
})
export class FactLostRouteTagsComponent {
}
