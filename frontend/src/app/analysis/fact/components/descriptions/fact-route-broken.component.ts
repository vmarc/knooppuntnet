import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-fact-route-broken',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <p i18n="@@fact.description.route-broken">TODO</p> `,
})
export class FactRouteBrokenComponent {}
