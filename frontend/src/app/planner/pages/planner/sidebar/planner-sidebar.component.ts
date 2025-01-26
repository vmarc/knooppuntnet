import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { PageFooterComponent } from '@app/components/shared/page';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { PlannerPageService } from '../planner-page.service';
import { PlanActionsComponent } from './plan-actions.component';
import { PlannerSideBarAppearanceComponent } from './planner-side-bar-appearance.component';
import { PlannerSideBarLegendComponent } from './planner-side-bar-legend.component';
import { PlannerSideBarOptionsComponent } from './planner-side-bar-options.component';
import { PlannerSideBarPlannerComponent } from './planner-side-bar-planner.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-planner-sidebar',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <kpn-plan-actions />

    <div>
      <button nz-button class="button-with-text" (click)="zoomToFitRoute()">
        Zoom to fit entire route
      </button>
    </div>

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
    NzButtonComponent,
    PageFooterComponent,
    PlanActionsComponent,
    PlannerSideBarAppearanceComponent,
    PlannerSideBarLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSideBarPlannerComponent,
    SidebarFooterComponent,
  ],
})
export class PlannerSidebarComponent {
  readonly service = inject(PlannerPageService);

  zoomToFitRoute(): void {
    this.service.zoomInToRoute();
  }
}
