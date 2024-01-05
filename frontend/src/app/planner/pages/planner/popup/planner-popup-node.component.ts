import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MapNodeDetail } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { OlUtil } from '@app/ol';
import { MapZoomService } from '@app/ol/services';
import { LinkRouteComponent } from '@app/components/shared/link';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { ApiService } from '@app/services';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { combineLatestWith } from 'rxjs';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { PlannerService } from '../../../planner.service';
import { MapService } from '../../../services/map.service';
import { selectPlannerNetworkType } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-planner-popup-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (response$ | async; as response) {
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
  imports: [AsyncPipe, LinkRouteComponent, RouterLink, TimestampComponent],
})
export class PlannerPopupNodeComponent implements OnInit, OnDestroy {
  private readonly apiService = inject(ApiService);
  private readonly mapService = inject(MapService);
  private readonly mapZoomService = inject(MapZoomService);
  private readonly plannerService = inject(PlannerService);
  private readonly store = inject(Store);

  protected response$: Observable<ApiResponse<MapNodeDetail>>;
  private zoomLevel = 0;
  private readonly subscriptions = new Subscriptions();

  ngOnInit(): void {
    this.response$ = this.mapService.nodeClicked$.pipe(
      filter((nodeClick) => nodeClick !== null),
      combineLatestWith(this.store.select(selectPlannerNetworkType)),
      switchMap(([nodeClick, networkType]) =>
        this.apiService.mapNodeDetail(networkType, +nodeClick.node.node.nodeId).pipe(
          tap((response) => {
            if (response.result) {
              const coordinate = OlUtil.toCoordinate(
                response.result.latitude,
                response.result.longitude
              );
              this.openPopup(coordinate);
            }
          })
        )
      )
    );

    this.subscriptions.add(
      this.mapZoomService.zoomLevel$.subscribe((z) => {
        this.zoomLevel = z;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private openPopup(coordinate: Coordinate): void {
    const verticalOffset = this.zoomLevel <= 13 ? -13 : -24;
    setTimeout(
      () => this.plannerService.context.overlay.setPosition(coordinate, verticalOffset),
      0
    );
  }
}
