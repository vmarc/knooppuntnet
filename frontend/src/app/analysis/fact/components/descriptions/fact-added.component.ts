import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-added',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <p i18n="@@fact.description.added">Added to the analysis.</p> `,
  standalone: true,
})
export class FactAddedComponent {}
