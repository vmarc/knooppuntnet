import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NodePage} from "../../../kpn/shared/node/node-page";
import {AppService} from "../../../app.service";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-detail-node',
  template: `
    <h2>
      Node <!-- Knooppunt --> {{nodeName}}
    </h2>

    <div *ngIf="nodeInfo">
      <a class="text" [routerLink]="'/analysis/node/' + nodeId">More details</a>

      <p>
        Last updated: <!-- "Laatst bewerkt" -->
        <br>
        <kpn-timestamp [timestamp]="nodeInfo.lastUpdated"></kpn-timestamp>
      </p>

      <div *ngIf="references.networkReferences.length > 0">
        Network(s): <!-- "Netwerken" -->
        <div *ngFor="let network of references.networkReferences">
          <a class="text" [routerLink]="'/analysis/network-details/' + network.id">{{network.name}}</a>
        </div>
      </div>

      <div *ngIf="references.routeReferences.length > 0">
        Routes <!-- "Routes" -->
        <div *ngFor="let route of references.routeReferences">
          <a class="text" [routerLink]="'/analysis/route/' + route.id">{{route.name}}</a>
        </div>
      </div>

      <json [object]="response"></json>

    </div>
  `
})
export class MapDetailNodeComponent implements OnChanges {

  @Input() nodeId: number;
  @Input() nodeName: string;
  @Input() networkType: NetworkType;

  response: ApiResponse<NodePage>;

  constructor(private appService: AppService) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["nodeId"]) {
      this.response = null;
      this.appService.node("" + this.nodeId).subscribe(response => {
        this.response = response;
      });
    }
  }

  get nodeInfo() {
    if (this.response && this.response.result) {
      return this.response.result.nodeInfo;
    }
    return null;
  }

  get references() {
    if (this.response && this.response.result) {
      return this.response.result.references;
    }
    return null;
  }

  get routeReferences() {
    if (this.response && this.response.result) {
      return this.response.result.references;
    }
    return null;
  }

}
