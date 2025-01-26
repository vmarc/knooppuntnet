import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { PlannerPopupService } from '../../planner/domain/context/planner-popup-service';
import { PlannerMapLayerService } from '../../planner/pages/planner/planner-map-layer.service';
import { PlannerMapService } from '../../planner/pages/planner/planner-map.service';
import { PlannerPageService } from '../../planner/pages/planner/planner-page.service';
import { PlannerStateService } from '../../planner/pages/planner/planner-state.service';
import { PlannerService } from '../../planner/pages/planner/planner.service';
import { PlannerSidebarComponent } from '../../planner/pages/planner/sidebar/planner-sidebar.component';
import { RouterService } from '../../shared/services/router.service';

@Component({
  selector: 'kpn-planner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button routerLink="/">
      <mat-icon svgIcon="back" />
    </button>
    <kpn-planner-sidebar />
  `,
  imports: [MatIcon, MatIconButton, RouterLink, PlannerSidebarComponent],
  providers: [
    PlannerPageService,
    PlannerStateService,
    RouterService,
    PlannerMapService,
    PlannerService,
    PlannerMapLayerService,
    PlannerPopupService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PlannerMapService,
    },
  ],
})
export class PlannerComponent {}
