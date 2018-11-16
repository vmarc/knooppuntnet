import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-become-ignored',
  template: `
    <!--Niet langer opgenomen in de analyse.-->
    <ng-container i18n="@@fact.description.become-ignored">
      No longer included in the analysis.
    </ng-container>
  `
})
export class FactBecomeIgnoredComponent {
}
