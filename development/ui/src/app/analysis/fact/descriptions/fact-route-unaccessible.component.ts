import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-unaccessible',
  template: `
    <!--Een deel van de route lijkt niet ",
    context.gotoGlossaryEntry("accessible", "toegankelijk"),
    "."
    -->
    Part of the route does not seem
    context.gotoGlossaryEntry("accessible", "accessible"),
    .
  `
})
export class FactRouteUnaccessibleComponent {
}
