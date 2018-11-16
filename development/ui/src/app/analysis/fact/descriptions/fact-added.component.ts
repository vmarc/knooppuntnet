import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-added',
  template: `
    <!--Toegevoegd aan de analyse.-->
    <ng-container i18n="@@fact.description.added">
      Added to the analysis.
    </ng-container>
  `
})
export class FactAddedComponent {
}
