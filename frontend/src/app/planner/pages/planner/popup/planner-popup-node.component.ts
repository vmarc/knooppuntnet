import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MapNodeDetail } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { LinkRouteComponent } from '@app/components/shared/link';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { OlUtil } from '@app/ol';
import { MapZoomService } from '@app/ol/services';
import { ApiService } from '@app/services';
import { Coordinate } from 'ol/coordinate';
import { PlannerPopupService } from '../../../domain/context/planner-popup-service';
import { PlannerService } from '../../../planner.service';
import { MapService } from '../../../services/map.service';

@Component({
  selector: 'kpn-planner-popup-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response(); as response) {
      @if (response.result) {
        <h2>
          <ng-container i18n="@@map.node-popup.title">Node</ng-container>
          {{ response.result.name }}
        </h2>
        <p>
          <span class="kpn-label" i18n="@@map.node-popup.last-updated">Last updated</span>
          <kpn-timestamp [timestamp]="response.result.lastUpdated" />
        </p>
        <div>
          @if (response.result.networkReferences.length === 1) {
            <span class="kpn-label" i18n="@@map.node-popup.network">Network</span>
          }
          @if (response.result.networkReferences.length !== 1) {
            <span class="kpn-label" i18n="@@map.node-popup.networks">Networks</span>
          }
          @if (response.result.networkReferences.length === 0) {
            <span i18n="@@map.node-popup.no-networks">None</span>
          }
          @for (ref of response.result.networkReferences; track ref) {
            <div class="reference">
              <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
            </div>
          }
        </div>
        @if (response.result.routeReferences.length > 0) {
          <div>
            <span class="kpn-label" i18n="@@map.node-popup.routes">Routes</span>
            @if (response.result.routeReferences.length === 0) {
              <span i18n="@@map.node-popup.routes.none">None</span>
            }
            @for (ref of response.result.routeReferences; track ref) {
              <div class="reference">
                <kpn-link-route
                  [routeId]="ref.id"
                  [routeName]="ref.name"
                  [networkType]="ref.networkType"
                />
              </div>
            }
          </div>
        }
        <div class="more-details">
          <a
            [routerLink]="'/analysis/node/' + response.result.id"
            i18n="@@map.node-popup.more-details"
          >
            More details
          </a>
        </div>
      }
    }
  `,
  styles: `
    .reference {
      margin: 0.5em 0 0.5em 1em;
    }

    .more-details {
      margin-top: 2em;
    }
  `,
  standalone: true,
  imports: [LinkRouteComponent, RouterLink, TimestampComponent],
})
export class PlannerPopupNodeComponent {
  private readonly service = inject(PlannerPopupService);
  private readonly apiService = inject(ApiService);
  private readonly mapService = inject(MapService);
  private readonly mapZoomService = inject(MapZoomService);
  private readonly plannerService = inject(PlannerService);

  protected response = signal<ApiResponse<MapNodeDetail>>(null);

  constructor() {
    effect(() => {
      const nodeClick = this.service.nodeClick();
      if (nodeClick !== null) {
        const networkType = this.mapService.networkType();
        const nodeId = +nodeClick.node.node.nodeId;
        this.apiService.mapNodeDetail(networkType, nodeId).subscribe((response) => {
          this.response.set(response);
          if (response.result) {
            const coordinate = OlUtil.toCoordinate(
              response.result.latitude,
              response.result.longitude
            );
            this.openPopup(coordinate);
          }
        });
      }
    });
  }

  private openPopup(coordinate: Coordinate): void {
    const verticalOffset = this.mapZoomService.zoomLevel() <= 13 ? -13 : -24;
    setTimeout(
      () => this.plannerService.context.plannerPopup.setPosition(coordinate, verticalOffset),
      0
    );
  }
}
