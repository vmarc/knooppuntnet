import {Component} from "@angular/core";

@Component({
  selector: "kpn-fact-route-unaccessible",
  template: `
    <markdown i18n="@@fact.description.route-unaccessible">
      Part of the route does not seem [accessible](/en/docs/en.html#_accessible).
    </markdown>
  `
})
export class FactRouteUnaccessibleComponent {
}
