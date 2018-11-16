import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-route-not-one-way',
  template: `
    <!--De route is getagged als bruikbaar in slechts 1 richting, maar de analyse logica heeft zowel een heenweg als een terugweg gevonden.-->
    The route is tagged as useable in one direction only, but the analysis logic does find ways in both directions.
  `
})
export class FactRouteNotOneWayComponent {
}
