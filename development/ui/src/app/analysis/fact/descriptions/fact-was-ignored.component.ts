import {Component} from '@angular/core';

@Component({
  selector: 'kpn-fact-was-ignored',
  template: `
    <ng-container i18n="@@fact.description.was-ignored">
      No longer excluded from the analysis.
    </ng-container>
  `
})
export class FactWasIgnoredComponent {
}
