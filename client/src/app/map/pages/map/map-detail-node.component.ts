import {Component, Input, OnChanges, SimpleChanges} from "@angular/core";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {NetworkType} from "../../../kpn/shared/network-type";
import {NodeDetailsPage} from "../../../kpn/shared/node/node-details-page";

@Component({
  selector: "kpn-map-detail-node",
  template: `
    <h2>
      Node <!--@@ Knooppunt --> {{nodeName}}
    </h2>

    <div *ngIf="nodeInfo">
      <a class="text" [routerLink]="'/analysis/node/' + nodeId">More details</a>

      <p>
        Last updated: <!--@@ "Laatst bewerkt" -->
        <br>
        <kpn-timestamp [timestamp]="nodeInfo.lastUpdated"></kpn-timestamp>
      </p>

      <div *ngIf="!references.networkReferences.isEmpty()">
        Network(s): <!--@@ "Netwerken" -->
        <div *ngFor="let ref of references.networkReferences">
          <a class="text" [routerLink]="'/analysis/network-details/' + ref.networkId">{{ref.networkName}}</a>
        </div>
      </div>

      <div *ngIf="!references.routeReferences.isEmpty()">
        Routes <!--@@ "Routes" -->
        <div *ngFor="let ref of references.routeReferences">
          <a class="text" [routerLink]="'/analysis/route/' + ref.routeId">{{ref.routeName}}</a>
        </div>
      </div>

      <json [object]="response"></json>

    </div>
  `
})
export class MapDetailNodeComponent implements OnChanges {

  @Input() nodeId: string;
  @Input() nodeName: string;
  @Input() networkType: NetworkType;

  response: ApiResponse<NodeDetailsPage>;

  constructor(private appService: AppService) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["nodeId"]) {
      this.response = null;
      this.appService.nodeDetails(this.nodeId).subscribe(response => {
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
