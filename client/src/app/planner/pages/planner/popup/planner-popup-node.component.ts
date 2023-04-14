import { OnDestroy } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapNodeDetail } from '@api/common/node';
import { ApiResponse } from '@api/custom';
import { AppService } from '@app/app.service';
import { MapZoomService } from '@app/components/ol/services';
import { MapService } from '@app/components/ol/services';
import { Util } from '@app/components/shared';
import { Subscriptions } from '@app/util';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { combineLatestWith } from 'rxjs';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { PlannerService } from '../../../planner.service';
import { selectPlannerNetworkType } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-planner-popup-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async as response">
      <h2>
        <ng-container i18n="@@map.node-popup.title">Node</ng-container>
        {{ response.result.name }}
      </h2>
      <p>
        <span class="kpn-label" i18n="@@map.node-popup.last-updated"
          >Last updated</span
        >
        <kpn-timestamp [timestamp]="response.result.lastUpdated" />
      </p>

      <div>
        <span
          *ngIf="response.result.networkReferences.length === 1"
          class="kpn-label"
          i18n="@@map.node-popup.network"
          >Network</span
        >
        <span
          *ngIf="response.result.networkReferences.length !== 1"
          class="kpn-label"
          i18n="@@map.node-popup.networks"
          >Networks</span
        >
        <span
          *ngIf="response.result.networkReferences.length === 0"
          i18n="@@map.node-popup.no-networks"
          >None</span
        >
        <div
          *ngFor="let ref of response.result.networkReferences"
          class="reference"
        >
          <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
        </div>
      </div>

      <div *ngIf="response.result.routeReferences.length > 0">
        <span class="kpn-label" i18n="@@map.node-popup.routes">Routes</span>
        <span
          *ngIf="response.result.routeReferences.length === 0"
          i18n="@@map.node-popup.routes.none"
          >None</span
        >
        <div
          *ngFor="let ref of response.result.routeReferences"
          class="reference"
        >
          <kpn-link-route
            [routeId]="ref.id"
            [title]="ref.name"
            [networkType]="ref.networkType"
          />
        </div>
      </div>

      <div class="more-details">
        <a
          [routerLink]="'/analysis/node/' + response.result.id"
          i18n="@@map.node-popup.more-details"
        >
          More details
        </a>
      </div>
    </div>
  `,
  styles: [
    `
      .reference {
        margin: 0.5em 0 0.5em 1em;
      }

      .more-details {
        margin-top: 2em;
      }
    `,
  ],
})
export class PlannerPopupNodeComponent implements OnInit, OnDestroy {
  response$: Observable<ApiResponse<MapNodeDetail>>;
  private zoomLevel = 0;
  private readonly subscriptions = new Subscriptions();

  constructor(
    private appService: AppService,
    private mapService: MapService,
    private mapZoomService: MapZoomService,
    private plannerService: PlannerService,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.response$ = this.mapService.nodeClicked$.pipe(
      filter((nodeClick) => nodeClick !== null),
      combineLatestWith(this.store.select(selectPlannerNetworkType)),
      switchMap(([nodeClick, networkType]) =>
        this.appService
          .mapNodeDetail(networkType, +nodeClick.node.node.nodeId)
          .pipe(
            tap((response) => {
              if (response.result) {
                const coordinate = Util.toCoordinate(
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
      () =>
        this.plannerService.context.overlay.setPosition(
          coordinate,
          verticalOffset
        ),
      0
    );
  }
}
