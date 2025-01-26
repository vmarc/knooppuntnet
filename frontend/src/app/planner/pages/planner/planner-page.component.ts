import { inject } from '@angular/core';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouterService } from '../../../shared/services/router.service';
import { PlannerPopupService } from '../../domain/context/planner-popup-service';
import { PlannerStateService } from './planner-state.service';
import { PlannerService } from './planner.service';
import { PlannerMapLayerService } from './planner-map-layer.service';
import { PlannerMapService } from './planner-map.service';
import { PlannerPageService } from './planner-page.service';
import { PoiMenuComponent } from './poi/poi-menu.component';
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
        <div [id]="service.mapId" class="map" (mouseleave)="service.mouseleave()">
          <kpn-layer-switcher>
            <kpn-poi-menu />
          </kpn-layer-switcher>
          <kpn-map-link-menu />
        </div>
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
  imports: [
    LayerSwitcherComponent,
    MapLinkMenuComponent,
    PlannerPopupComponent,
    PlannerSidebarComponent,
    PoiMenuComponent,
  ],
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
