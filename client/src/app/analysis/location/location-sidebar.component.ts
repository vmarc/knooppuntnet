import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-location-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-sidebar> </kpn-sidebar> `,
})
export class LocationSidebarComponent {}
