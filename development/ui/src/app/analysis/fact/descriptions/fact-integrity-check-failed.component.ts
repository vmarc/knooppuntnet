import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-integrity-check-failed',
  template: `
    <!--Het aantal routes die in het knooppunt aankomen of vertrekken is niet het verwachte aantal.
    |Routes met tag "state" gelijk aan "connection" of "alternate" worden niet meegeteld.-->
    The actual number of routes does not match the expected number of routes. Routes with
    tag "state" equal to "connection or "alternate" are not counted.
  `
})
export class FactIntegrityCheckFailedComponent {
}
