import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-location-selection-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <kpn-location-analysis-strategy></kpn-location-analysis-strategy>
      <kpn-location-mode></kpn-location-mode>
    </kpn-sidebar>
  `,
})
export class LocationSelectionSidebarComponent {}
