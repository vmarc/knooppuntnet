import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarComponent } from '@app/shared/components/sidebar/sidebar.component';

@Component({
  selector: 'kpn-location-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: '<kpn-sidebar/>',
  imports: [SidebarComponent],
})
export class LocationSidebarComponent {}
