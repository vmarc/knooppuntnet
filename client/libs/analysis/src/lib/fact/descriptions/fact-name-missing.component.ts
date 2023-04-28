import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-name-missing',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n="@@fact.description.name-missing">
      The network relation does not contain the mandatory tag with key "name".
    </p>
  `,
  standalone: true,
})
export class FactNameMissingComponent {}
