import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'ui-network-not-found',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<p i18n="@@network-page.network-not-found">Network not found</p>`,
})
export class NetworkNotFoundComponent {}
