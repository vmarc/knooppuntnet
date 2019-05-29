import {Component, Input} from "@angular/core";
import {MapService} from "src/app/components/ol/map.service";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: "kpn-map-sidebar-analysis",
  template: `

    <mat-expansion-panel expanded="true">
      <mat-expansion-panel-header>
        <h1>Quality analysis</h1>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>

        <div class="title">
          <kpn-network-type-icon [networkType]="networkType"></kpn-network-type-icon>
          <kpn-network-type-name [networkType]="networkType"></kpn-network-type-name>
        </div>

        <kpn-map-detail-default *ngIf="isDefault()">
        </kpn-map-detail-default>

        <kpn-map-detail-node
          *ngIf="isNodeSelected()"
          [nodeId]="selectedFeature().featureId"
          [nodeName]="selectedFeature().name"
          [networkType]="networkType">
        </kpn-map-detail-node>

        <kpn-map-detail-route
          *ngIf="isRouteSelected()"
          [routeId]="selectedFeature().featureId"
          [routeName]="selectedFeature().name">
        </kpn-map-detail-route>

      </ng-template>
    </mat-expansion-panel>

  `
})
export class MapSidebarAnalysisComponent {

  @Input() networkType: NetworkType;

  constructor(private mapService: MapService) {
  }

  isDefault(): boolean {
    return !(this.isNodeSelected() || this.isRouteSelected());
  }

  isNodeSelected(): boolean {
    return this.selectedFeature() && this.selectedFeature().featureType === "node";
  }

  isRouteSelected(): boolean {
    return this.selectedFeature() && this.selectedFeature().featureType === "route";
  }

  selectedFeature() {
    return this.mapService.selectedFeature.value;
  }
}
