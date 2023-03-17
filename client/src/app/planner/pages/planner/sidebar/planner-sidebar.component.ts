import { Component } from '@angular/core';

@Component({
  selector: 'kpn-planner-sidebar',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-planner-sidebar-planner/>
    <kpn-planner-sidebar-appearance/>
    <kpn-planner-sidebar-legend/>
    <kpn-planner-sidebar-options/>
    <!--
      <kpn-elevation-profile></kpn-elevation-profile>
      <kpn-planner-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
    -->
    <kpn-sidebar-footer [loginEnabled]="false"/>
    <kpn-page-footer [settings]="false"/>
  `,
})
export class PlannerSidebarComponent {}
