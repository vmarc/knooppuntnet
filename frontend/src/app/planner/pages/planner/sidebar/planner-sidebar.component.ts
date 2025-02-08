import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components/map-link-menu.component';
import { State } from '@app/state';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { GeolocationButtonComponent } from '../geolocation/geolocation-button.component';
import { PlanActionsComponent } from './plan-actions.component';
import { PlanComponent } from './plan.component';
import { PlannerSideBarLegendComponent } from './planner-side-bar-legend.component';
import { PlannerSideBarOptionsComponent } from './planner-side-bar-options.component';
import { ChangeDetectionStrategy } from '@angular/core';
import { PlannerSidebarFitRouteComponent } from './planner-sidebar-fit-route.component';

@Component({
  selector: 'kpn-planner-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-plan-actions />
    <kpn-planner-fit-route />
    <kpn-geolocation-button />
    <kpn-map-link-menu />

    <nz-collapse>
      <nz-collapse-panel nzHeader="Plan" [nzActive]="true">
        <kpn-plan />
      </nz-collapse-panel>

      <nz-collapse>
        <nz-collapse-panel
          i18n-nzHeader="@@planner.legend"
          nzHeader="Legend"
          [nzActive]="legendExpanded()"
          (nzActiveChange)="legendExpandedChanged($event)"
        >
          <kpn-planner-sidebar-legend />
        </nz-collapse-panel>
        <nz-collapse-panel
          i18n-nzHeader="@@planner.options"
          nzHeader="Options"
          [nzActive]="optionsExpanded()"
          (nzActiveChange)="optionsExpandedChanged($event)"
        >
          <kpn-planner-sidebar-options />
        </nz-collapse-panel>
      </nz-collapse>

      <!--
        <kpn-elevation-profile />
        <kpn-planner-sidebar-poi-configuration />
      -->
    </nz-collapse>
  `,
  imports: [
    GeolocationButtonComponent,
    MapLinkMenuComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    PlanActionsComponent,
    PlannerSideBarLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSidebarFitRouteComponent,
    PlanComponent,
  ],
})
export class PlannerSidebarComponent {
  private readonly state = inject(State);
  readonly legendExpanded = this.state.preferences.showLegend;
  readonly optionsExpanded = this.state.preferences.showOptions;

  legendExpandedChanged(expanded: boolean): void {
    this.state.preferences.updateShowLegend(expanded);
  }

  optionsExpandedChanged(expanded: boolean): void {
    this.state.preferences.updateShowOptions(expanded);
  }
}
