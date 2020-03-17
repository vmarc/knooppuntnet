import {OnDestroy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {flatMap} from "rxjs/operators";
import {filter} from "rxjs/operators";
import {tap} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {NodeClick} from "../../../../components/ol/domain/node-click";
import {MapService} from "../../../../components/ol/map.service";
import {MapNodeDetail} from "../../../../kpn/api/common/node/map-node-detail";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {Subscriptions} from "../../../../util/Subscriptions";
import {PlannerService} from "../../../planner.service";

@Component({
  selector: "kpn-map-popup-node",
  template: `
    <h2>
      <ng-container i18n="@@map.node-popup.title">Node</ng-container>
      {{nodeClick.node.nodeName}}
    </h2>

    <div *ngIf="response?.result">
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
          [routerLink]="'/analysis/node/' + nodeClick.node.nodeId"
          [state]="{nodeName: nodeClick.node.nodeName}"
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

  nodeClick: NodeClick;

  response: ApiResponse<MapNodeDetail>;
  private readonly subscriptions = new Subscriptions();

  constructor(private appService: AppService,
              private mapService: MapService,
              private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.subscriptions.add(
      this.mapService.nodeClicked.pipe(
        tap(nodeClick => this.nodeClick = nodeClick),
        filter(nodeClick => nodeClick !== null),
        flatMap(() => this.appService.mapNodeDetail(this.mapService.networkType.value, this.nodeClick.node.nodeId))
      ).subscribe(response => {
        this.response = response;
        this.plannerService.context.overlay.setPosition(this.nodeClick.coordinate);
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
