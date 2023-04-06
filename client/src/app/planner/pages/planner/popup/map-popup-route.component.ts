import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MapRouteDetail } from '@api/common/route/map-route-detail';
import { ApiResponse } from '@api/custom/api-response';
import { AppService } from '@app/app.service';
import { MapService } from '@app/components/ol/services/map.service';
import { Coordinate } from 'ol/coordinate';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { PlannerService } from '@app/services/planner.service';

@Component({
  selector: 'kpn-planner-popup-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async as response">
      <h2>
        <span i18n="@@map.route-popup.title">Route</span>
        {{ response.result.name }}
      </h2>

      <div>
        <span
          *ngIf="response.result.networkReferences.length === 1"
          class="kpn-label"
          i18n="@@map.route-popup.network"
          >Network</span
        >
        <span
          *ngIf="response.result.networkReferences.length !== 1"
          class="kpn-label"
          i18n="@@map.route-popup.networks"
          >Networks</span
        >
        <span
          *ngIf="response.result.networkReferences.length === 0"
          i18n="@@map.route-popup.no-networks"
          >None</span
        >
        <div
          *ngFor="let ref of response.result.networkReferences"
          class="reference"
        >
          <a [routerLink]="'/analysis/network/' + ref.id">{{ ref.name }}</a>
        </div>
      </div>

      <p class="more-details">
        <kpn-link-route
          [routeId]="response.result.id"
          title="More details"
          i18n-title="@@map.route-popup.more-details"
        />
      </p>
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
export class MapPopupRouteComponent implements OnInit {
  response$: Observable<ApiResponse<MapRouteDetail>>;

  constructor(
    private appService: AppService,
    private mapService: MapService,
    private plannerService: PlannerService
  ) {}

  ngOnInit(): void {
    this.response$ = this.mapService.routeClicked$.pipe(
      filter((routeClick) => routeClick !== null),
      switchMap((routeClick) =>
        this.appService.mapRouteDetail(routeClick.route.routeId).pipe(
          tap((response) => {
            if (response.result) {
              this.openPopup(routeClick.coordinate);
            }
          })
        )
      )
    );
  }

  private openPopup(coordinate: Coordinate): void {
    setTimeout(
      () => this.plannerService.context.overlay.setPosition(coordinate, -12),
      0
    );
  }
}
