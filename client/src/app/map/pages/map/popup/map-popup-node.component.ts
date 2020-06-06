import {OnDestroy} from "@angular/core";
import {OnInit} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Coordinate} from "ol/coordinate";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {MapZoomService} from "../../../../components/ol/services/map-zoom.service";
import {MapService} from "../../../../components/ol/services/map.service";
import {Util} from "../../../../components/shared/util";
import {MapNodeDetail} from "../../../../kpn/api/common/node/map-node-detail";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {PlannerService} from "../../../planner.service";

@Component({
  selector: "kpn-map-popup-node",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response$ | async as response">
      <h2>
        <ng-container i18n="@@map.node-popup.title">Node</ng-container>
        {{response.result.name}}
      </h2>
      <p>
        <span class="kpn-label" i18n="@@map.node-popup.title">Last updated</span>
        <kpn-timestamp [timestamp]="response.result.lastUpdated"></kpn-timestamp>
      </p>

      <div>
        <span *ngIf="response.result.networkReferences.size === 1" class="kpn-label" i18n="@@map.node-popup.network">Network</span>
        <span *ngIf="response.result.networkReferences.size !== 1" class="kpn-label" i18n="@@map.node-popup.networks">Networks</span>
        <span *ngIf="response.result.networkReferences.isEmpty()" i18n="@@map.node-popup.no-networks">None</span>
        <div *ngFor="let ref of response.result.networkReferences" class="reference">
          <a [routerLink]="'/analysis/network/' + ref.id">{{ref.name}}</a>
        </div>
      </div>

      <div *ngIf="!response.result.routeReferences.isEmpty()">
        <span class="kpn-label" i18n="@@map.node-popup.routes">Routes</span>
        <span *ngIf="response.result.routeReferences.isEmpty()" i18n="@@map.node-popup.routes.none">None</span>
        <div *ngFor="let ref of response.result.routeReferences" class="reference">
          <a [routerLink]="'/analysis/route/' + ref.id">{{ref.name}}</a>
        </div>
      </div>

      <div class="more-details">
        <a
          [routerLink]="'/analysis/node/' + response.result.id"
          [state]="{nodeName: response.result.name}"
          i18n="@@map.node-popup.more-details">
          More details
        </a>
      </div>

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
export class MapPopupNodeComponent implements OnInit, OnDestroy {
  response$: Observable<ApiResponse<MapNodeDetail>>;
  private zoomLevel = 0;
  private readonly subscriptions = new Subscriptions();

  constructor(private appService: AppService,
              private mapService: MapService,
              private mapZoomService: MapZoomService,
              private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.response$ = this.mapService.nodeClicked$.pipe(
      filter(nodeClick => nodeClick !== null),
      switchMap(nodeClick =>
        this.appService.mapNodeDetail(this.mapService.networkType(), nodeClick.node.nodeId).pipe(
          tap(response => {
            const coordinate = Util.toCoordinate(response.result.latitude, response.result.longitude);
            this.openPopup(coordinate);
          })
        )
      )
    );

    this.subscriptions.add(
      this.mapZoomService.zoomLevel$.subscribe(z => {
        this.zoomLevel = z;
        console.log("zoomlevel=" + this.zoomLevel);
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private openPopup(coordinate: Coordinate): void {
    const verticalOffset = this.zoomLevel <= 13 ? -13 : -24;
    setTimeout(() => this.plannerService.context.overlay.setPosition(coordinate, verticalOffset), 0);
  }
}
