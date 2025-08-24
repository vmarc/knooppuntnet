import { OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components/map-link-menu.component';
import { MAP_SERVICE_TOKEN } from '@app/ol/services/openlayers-map-service';
import { GeolocationButtonComponent } from '@app/planner/pages/planner/geolocation/geolocation-button.component';
import { PlanActionsComponent } from '@app/planner/pages/planner/sidebar/plan-actions.component';
import { PlanComponent } from '@app/planner/pages/planner/sidebar/plan.component';
import { PlannerLegendComponent } from '@app/planner/pages/planner/legend/planner-legend.component';
import { PlannerSideBarOptionsComponent } from '@app/planner/pages/planner/sidebar/planner-side-bar-options.component';
import { PlannerSideBarPoiConfigurationComponent } from '@app/planner/pages/planner/sidebar/planner-side-bar-poi-configuration.component';
import { PlannerSidebarFitRouteComponent } from '@app/planner/pages/planner/sidebar/planner-sidebar-fit-route.component';
import { BreadcrumbItem } from '@app/shared/components/breadcrumb/breadcrumb-item';
import { BreadcrumbComponent } from '@app/shared/components/breadcrumb/breadcrumb.component';
import { Breadcrumbs } from '@app/shared/components/breadcrumb/breadcrumbs';
import { PageComponent } from '@app/shared/components/page/page.component';
import { State } from '@app/state/state';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';

@Component({
  selector: 'ui-planner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-page>
      <ui-breadcrumb [breadcrumbItems]="breadcrumbItems" />
      <ui-plan-actions />
      <ui-planner-fit-route />
      <ui-geolocation-button />
      <ui-map-link-menu />
    </ui-page>

    <nz-collapse>
      <nz-collapse-panel nzHeader="Plan" [nzActive]="true">
        <ui-plan />
      </nz-collapse-panel>

      <nz-collapse>
        <nz-collapse-panel
          i18n-nzHeader="@@planner.legend"
          nzHeader="Legend"
          [nzActive]="legendExpanded()"
          (nzActiveChange)="legendExpandedChanged($event)"
        >
          <ui-planner-legend />
        </nz-collapse-panel>
        <nz-collapse-panel
          i18n-nzHeader="@@planner.options"
          nzHeader="Options"
          [nzActive]="optionsExpanded()"
          (nzActiveChange)="optionsExpandedChanged($event)"
        >
          <ui-planner-sidebar-options />
        </nz-collapse-panel>
      </nz-collapse>

      <!--
        <ui-elevation-profile />
      -->
      <ui-planner-sidebar-poi-configuration />
    </nz-collapse>
  `,
  providers: [
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PlannerMapService,
    },
  ],
  imports: [
    BreadcrumbComponent,
    GeolocationButtonComponent,
    MapLinkMenuComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    PageComponent,
    PlanActionsComponent,
    PlanComponent,
    PlannerLegendComponent,
    PlannerSideBarOptionsComponent,
    PlannerSidebarFitRouteComponent,
    PlannerSideBarPoiConfigurationComponent,
  ],
})
export class PlannerComponent implements OnInit {
  private readonly state = inject(State);
  protected readonly legendExpanded = this.state.preferences.showLegend;
  protected readonly optionsExpanded = this.state.preferences.showOptions;

  protected readonly breadcrumbItems: BreadcrumbItem[] = [
    Breadcrumbs.home,
    { label: Breadcrumbs.planLabel },
  ];

  ngOnInit(): void {
    this.state.map.updateSubject('plan');
  }

  legendExpandedChanged(expanded: boolean): void {
    this.state.preferences.updateShowLegend(expanded);
  }

  optionsExpandedChanged(expanded: boolean): void {
    this.state.preferences.updateShowOptions(expanded);
  }
}
