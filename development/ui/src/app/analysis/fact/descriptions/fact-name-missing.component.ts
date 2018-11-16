import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-name-missing',
  template: `
    <!--De netwerk relatie bevat geen verplichte tag met sleutel "name".-->
    The network relation does not contain the mandatory tag with key "name".
  `
})
export class FactNameMissingComponent {
}
