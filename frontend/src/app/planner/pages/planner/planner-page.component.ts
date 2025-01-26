import { inject } from '@angular/core';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouterService } from '../../../shared/services/router.service';
import { PlannerPopupService } from '../../domain/context/planner-popup-service';
import { PlannerStateService } from './planner-state.service';
import { PlannerService } from './planner.service';
import { PlannerMapLayerService } from './planner-map-layer.service';
import { PlannerMapService } from './planner-map.service';
import { PlannerPageService } from './planner-page.service';
import { PlannerPopupComponent } from './popup/planner-popup.component';
import { PlannerSidebarComponent } from './sidebar/planner-sidebar.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-planner-page',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div style="display:flex;flex-direction: column">
      <div style="width: 300px">
        <kpn-planner-sidebar />
      </div>
      <div>
        <kpn-planner-popup />
        <div [id]="service.mapId" class="map" (mouseleave)="service.mouseleave()"></div>
      </div>
    </div>
  `,
  styles: `
    .map {
      position: absolute;
      top: 48px;
      left: 300px;
      right: 10px;
      bottom: 0;
      background-color: white;
      overflow: hidden;
    }

    .map:-webkit-full-screen {
      top: 0;
    }
  `,
  providers: [
    PlannerMapLayerService,
    PlannerMapService,
    PlannerPageService,
    PlannerPopupService,
    PlannerService,
    PlannerStateService,
    RouterService,
    {
      provide: MAP_SERVICE_TOKEN,
      useExisting: PlannerMapService,
    },
  ],
  imports: [PlannerPopupComponent, PlannerSidebarComponent],
})
export class PlannerPageComponent implements OnInit, OnDestroy, AfterViewInit {
  readonly service = inject(PlannerPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  ngOnDestroy(): void {
    this.service.onDestroy();
  }

  ngAfterViewInit(): void {
    this.service.afterViewInit();
  }
}
