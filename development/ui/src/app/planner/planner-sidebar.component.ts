import {Component, Input} from '@angular/core';
import {NetworkType} from "../kpn/shared/network-type";
import {SelectedFeature} from "../map/domain/selected-feature";

@Component({
  selector: 'kpn-planner-sidebar',
  template: `

    <div>
    <mat-button-toggle-group  [value]="networkType.name">
      <mat-button-toggle value="rcn" aria-label="Bicycle node networks">
        <mat-icon>directions_bike</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="rwn">
        <mat-icon>directions_walk</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="rhn">
        <mat-icon>format_align_right</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="rmn">
        <mat-icon>format_align_justify</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="rpn">
        <mat-icon>format_align_right</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="rin">
        <mat-icon>format_align_justify</mat-icon>
      </mat-button-toggle>
    </mat-button-toggle-group>
    </div>


    <div>
    <mat-button-toggle-group  [value]="'planner'">
      <mat-button-toggle value="planner">
        <mat-icon>format_align_right</mat-icon>
        Route planner
      </mat-button-toggle>
      <mat-button-toggle value="analysis">
        <mat-icon>format_align_justify</mat-icon>
        Analysis
      </mat-button-toggle>
    </mat-button-toggle-group>
    </div>


    <mat-radio-group [value]="analysisMode" >
      <mat-radio-button value="status" class="mode-radio-button">
        Node and route status
      </mat-radio-button>
      <mat-radio-button value="survey" class="mode-radio-button">
        Date last survey
      </mat-radio-button>
    </mat-radio-group>
    
    
    <div class="title">
      <kpn-network-type-icon [networkType]="networkType"></kpn-network-type-icon>
      <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
    </div>


    <kpn-map-detail-default *ngIf="isDefault()">
    </kpn-map-detail-default>

    <kpn-map-detail-node
      *ngIf="isNodeSelected()"
      [nodeId]="selectedFeature.featureId"
      [nodeName]="selectedFeature.name"
      [networkType]="networkType">
    </kpn-map-detail-node>

    <kpn-map-detail-route
      *ngIf="isRouteSelected()"
      [routeId]="selectedFeature.featureId"
      [routeName]="selectedFeature.name">
    </kpn-map-detail-route>


    <kpn-map-poi-config></kpn-map-poi-config>

  `,
  styles: [`
    .title {
      display: flex;
      line-height: 20px;
      flex-direction: row;
      align-items: center;
    }
    
    .mode-radio-button {
      display: block;
      padding: 5px;
    }
  `]
})
export class PlannerSidebarComponent {

// <kpn-map-detail sidenav [selectedFeature]="selectedFeature" [networkType]="networkType"></kpn-map-detail>

  @Input() selectedFeature: SelectedFeature;
  @Input() networkType: NetworkType = new NetworkType("rcn"); // TODO cleanup

  analysisMode = "status";

  isDefault(): boolean {
    return !(this.isNodeSelected() || this.isRouteSelected());
  }

  isNodeSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "node";
  }

  isRouteSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "route";
  }
}
