import {ChangeDetectorRef} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {MapService} from "../../../../components/ol/services/map.service";
import {MapRouteDetail} from "../../../../kpn/api/common/route/map-route-detail";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {PlannerService} from "../../../planner.service";

@Component({
  selector: "kpn-map-popup-route",
  template: `
    <div *ngIf="response$ | async as response">
      <h2>
        <span i18n="@@map.route-popup.title">Route</span> {{response.result.name}}
      </h2>

      <div>
        <span *ngIf="response.result.networkReferences.size === 1" class="kpn-label" i18n="@@map.route-popup.network">Network</span>
        <span *ngIf="response.result.networkReferences.size !== 1" class="kpn-label" i18n="@@map.route-popup.networks">Networks</span>
        <span *ngIf="response.result.networkReferences.isEmpty()" i18n="@@map.route-popup.no-networks">None</span>
        <div *ngFor="let ref of response.result.networkReferences" class="reference">
          <a [routerLink]="'/analysis/network/' + ref.id">{{ref.name}}</a>
        </div>
      </div>

      <p class="more-details">
        <kpn-link-route [routeId]="response.result.id" title="More details" i18n-title="@@map.route-popup.more-details"></kpn-link-route>
      </p>

    </div>
  `,
  styles: [`
    .reference {
      margin: 0.5em 0 0.5em 1em;
    }

    .more-details {
      margin-top: 2em;
    }
  `]
})
export class MapPopupRouteComponent {

  readonly response$: Observable<ApiResponse<MapRouteDetail>>;

  constructor(private appService: AppService,
              private mapService: MapService,
              private plannerService: PlannerService,
              private cdr: ChangeDetectorRef) {
    this.response$ = this.mapService.routeClicked.pipe(
      tap(() => console.log("route clicked")),
      filter(routeClick => routeClick !== null),
      switchMap(routeClick =>
        this.appService.mapRouteDetail(routeClick.route.routeId).pipe(
          tap(xx => console.log("route info received")),
          tap(response => {
            this.cdr.detectChanges();
            this.plannerService.context.overlay.setPosition(routeClick.coordinate, 0);
          })
        )
      )
    );
  }
}
