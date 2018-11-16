import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-ignore-foreign-country',
  template: `
    <!--Niet opgenomen in analyse: bevind zich in ander land dan Nederland, België of Duitsland.-->
    Not included in analysis: located in a country different from The Netherlands, Belgium or Germany.
  `
})
export class FactIgnoreForeignCountryComponent {
}
