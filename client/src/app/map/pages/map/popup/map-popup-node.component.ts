import {ChangeDetectorRef} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {switchMap} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {MapService} from "../../../../components/ol/map.service";
import {Util} from "../../../../components/shared/util";
import {MapNodeDetail} from "../../../../kpn/api/common/node/map-node-detail";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {PlannerService} from "../../../planner.service";

@Component({
  selector: "kpn-map-popup-node",
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
export class MapPopupNodeComponent {

  readonly response$: Observable<ApiResponse<MapNodeDetail>>;

  constructor(private appService: AppService,
              private mapService: MapService,
              private plannerService: PlannerService,
              private cdr: ChangeDetectorRef) {

    this.response$ = this.mapService.nodeClicked.pipe(
      tap(xx => console.log("node clicked")),
      filter(nodeClick => nodeClick !== null),
      switchMap(nodeClick =>
        this.appService.mapNodeDetail(this.mapService.networkType.value, nodeClick.node.nodeId).pipe(
          tap(xx => console.log("route info received")),
          tap(response => {
            this.cdr.detectChanges();
            const coordinate = Util.toCoordinate(response.result.latitude, response.result.longitude);
            this.plannerService.context.overlay.setPosition(coordinate, -10);
          })
        )
      )
    );
  }
}
