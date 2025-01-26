import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PageFooterComponent } from '@app/components/shared/page';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { GeolocationButtonComponent } from '../geolocation/geolocation-button.component';
import { PlannerPageService } from '../planner-page.service';
import { PlanActionsComponent } from './plan-actions.component';
import { PlannerSideBarAppearanceComponent } from './planner-side-bar-appearance.component';
import { PlannerSideBarLegendComponent } from './planner-side-bar-legend.component';
import { PlannerSideBarOptionsComponent } from './planner-side-bar-options.component';
import { PlannerSideBarPlannerComponent } from './planner-side-bar-planner.component';
import { ChangeDetectionStrategy } from '@angular/core';
import { PlannerSidebarFitRouteComponent } from './planner-sidebar-fit-route.component';

@Component({
  selector: 'kpn-planner-sidebar',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <kpn-plan-actions />
    <kpn-planner-fit-route />
    <kpn-geolocation-button />
    <kpn-planner-sidebar-planner />
    <kpn-planner-sidebar-appearance />
    <kpn-planner-sidebar-legend />
    <kpn-planner-sidebar-options />
    <!--
      <kpn-elevation-profile />
      <kpn-planner-sidebar-poi-configuration />
    -->
    <kpn-sidebar-footer [loginEnabled]="false" />
    <kpn-page-footer [settings]="false" />
  `,
  imports: [
    GeolocationButtonComponent,
    PageFooterComponent,
    PlanActionsComponent,
    PlannerSideBarAppearanceComponent,
    PlannerSideBarLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSideBarPlannerComponent,
    PlannerSidebarFitRouteComponent,
    SidebarFooterComponent,
  ],
})
export class PlannerSidebarComponent {
  readonly service = inject(PlannerPageService);

  zoomToFitRoute(): void {
    this.service.zoomInToRoute();
  }
}
