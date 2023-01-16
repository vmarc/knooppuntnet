import { Component } from '@angular/core';

@Component({
  selector: 'kpn-map-sidebar',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-map-sidebar-planner/>
    <kpn-map-sidebar-appearance/>
    <kpn-map-sidebar-legend/>
    <kpn-map-sidebar-options/>
    <!--
      <kpn-elevation-profile></kpn-elevation-profile>
      <kpn-map-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
    -->
    <kpn-sidebar-footer [loginEnabled]="false"/>
    <kpn-page-footer [settings]="false"/>
  `,
})
export class MapSidebarComponent {}
