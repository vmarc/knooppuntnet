import { inject } from '@angular/core';
import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { OldPageComponent } from '@app/components/shared/page';
import { MapLinkMenuComponent } from '@app/ol/components';
import { LayerSwitcherComponent } from '@app/ol/components';
import { RouteControlComponent } from '@app/ol/components';
import { MAP_SERVICE_TOKEN } from '@app/ol/services';
import { RouterService } from '../../../shared/services/router.service';
import { PlannerPopupService } from '../../domain/context/planner-popup-service';
import { PlannerStateService } from './planner-state.service';
import { PlannerService } from './planner.service';
import { PlannerMapLayerService } from './planner-map-layer.service';
import { GeolocationControlComponent } from './geolocation/geolocation-control.component';
import { PlannerMapService } from './planner-map.service';
import { PlannerPageService } from './planner-page.service';
import { PoiMenuComponent } from './poi/poi-menu.component';
import { PlannerPopupComponent } from './popup/planner-popup.component';
import { PlannerSidebarComponent } from './sidebar/planner-sidebar.component';
import { PlannerToolbarComponent } from './sidebar/planner-toolbar.component';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-planner-page',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <kpn-old-page>
      <kpn-planner-popup />
      <div [id]="service.mapId" class="map" (mouseleave)="service.mouseleave()">
        <kpn-route-control (action)="service.zoomInToRoute()" />
        <kpn-geolocation-control (action)="service.geolocation($event)" />
        <kpn-layer-switcher>
          <kpn-poi-menu />
        </kpn-layer-switcher>
        <kpn-map-link-menu />
      </div>
      <kpn-planner-toolbar toolbar />
      <kpn-planner-sidebar sidebar />
    </kpn-old-page>
  `,
  styles: `
    .map {
      position: absolute;
      top: 48px;
      left: 0;
      right: 0;
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
  standalone: true,
  imports: [
    GeolocationControlComponent,
    LayerSwitcherComponent,
    MapLinkMenuComponent,
    OldPageComponent,
    PlannerPopupComponent,
    PlannerSidebarComponent,
    PlannerToolbarComponent,
    PoiMenuComponent,
    RouteControlComponent,
  ],
})
export class PlannerPageComponent implements OnInit, OnDestroy, AfterViewInit {
  protected readonly service = inject(PlannerPageService);

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
