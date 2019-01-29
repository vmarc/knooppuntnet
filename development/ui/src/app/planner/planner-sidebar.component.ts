import {Component, Input} from '@angular/core';
import {NetworkType} from "../kpn/shared/network-type";
import {SelectedFeature} from "../map/domain/selected-feature";

@Component({
  selector: 'kpn-planner-sidebar',
  template: `

    <mat-button-toggle-group  [value]="networkType.name">
      <mat-button-toggle value="rcn">
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
  `,
  styles: [`
    .title {
      display: flex;
      line-height: 20px;
      flex-direction: row;
      align-items: center;
    }
  `]
})
export class PlannerSidebarComponent {

// <kpn-map-detail sidenav [selectedFeature]="selectedFeature" [networkType]="networkType"></kpn-map-detail>

  @Input() selectedFeature: SelectedFeature;
  @Input() networkType: NetworkType = new NetworkType("rcn"); // TODO cleanup

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
