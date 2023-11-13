import { Component } from '@angular/core';
import { PageFooterComponent } from '@app/components/shared/page';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { PlannerSideBarAppearanceComponent } from './planner-side-bar-appearance.component';
import { PlannerSideBarLegendComponent } from './planner-side-bar-legend.component';
import { PlannerSideBarOptionsComponent } from './planner-side-bar-options.component';
import { PlannerSideBarPlannerComponent } from './planner-side-bar-planner.component';

@Component({
  selector: 'kpn-planner-sidebar',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-planner-sidebar-planner />
    <kpn-planner-sidebar-appearance />
    <kpn-planner-sidebar-legend />
    <kpn-planner-sidebar-options />
    <!--
      <kpn-elevation-profile></kpn-elevation-profile>
      <kpn-planner-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
    -->
    <kpn-sidebar-footer [loginEnabled]="false" />
    <kpn-page-footer [settings]="false" />
  `,
  standalone: true,
  imports: [
    PageFooterComponent,
    PlannerSideBarAppearanceComponent,
    PlannerSideBarLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSideBarPlannerComponent,
    SidebarFooterComponent,
  ],
})
export class PlannerSidebarComponent {}
