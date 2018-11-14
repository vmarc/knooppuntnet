import {Component, Input} from '@angular/core';
import {SelectedFeature} from "../../../map/domain/selected-feature";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-detail',
  template: `
    <div class="kpn-sidenav">
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
    </div>
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
export class MapDetailComponent {

  @Input() selectedFeature: SelectedFeature;
  @Input() networkType: NetworkType;

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
