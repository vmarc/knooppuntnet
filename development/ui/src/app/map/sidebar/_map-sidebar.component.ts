import {Component, Input, OnInit} from '@angular/core';
import {MatButtonToggleChange} from "@angular/material";
import {MapService} from 'src/app/components/ol/map.service';
import {SelectedFeature} from "../../components/ol/domain/selected-feature";
import {NetworkType} from "../../kpn/shared/network-type";

@Component({
  selector: 'kpn-map-sidebar',
  template: `

    <kpn-network-type-selector></kpn-network-type-selector>

    <div>
      <mat-button-toggle-group [value]="pageMode" (change)="pageModeChanged($event)">
        <mat-button-toggle value="planner">
          <mat-icon svgIcon="map"></mat-icon>
          Planner
        </mat-button-toggle>
        <mat-button-toggle value="analysis">
          <mat-icon svgIcon="analysis"></mat-icon>
          Analysis
        </mat-button-toggle>
        <mat-button-toggle value="poi">
          <mat-icon svgIcon="help"></mat-icon>
          POI
        </mat-button-toggle>
      </mat-button-toggle-group>
    </div>

    <div *ngIf="pageMode === 'planner'">
      <kpn-map-sidebar-planner></kpn-map-sidebar-planner>
    </div>

    <kpn-map-sidebar-analysis *ngIf="pageMode === 'analysis'" [networkType]="networkType"></kpn-map-sidebar-analysis>

    <div *ngIf="pageMode === 'poi'">
      <kpn-poi-detail></kpn-poi-detail>
    </div>

    <ng-container *ngIf="pageMode !== 'poi'">
      <mat-divider></mat-divider>
      <kpn-map-sidebar-appearance></kpn-map-sidebar-appearance>
      <kpn-map-sidebar-legend></kpn-map-sidebar-legend>
      <kpn-map-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
    </ng-container>
  `
})
export class MapSidebarComponent implements OnInit {

  @Input() selectedFeature: SelectedFeature;
  @Input() networkType: NetworkType = new NetworkType("rcn"); // TODO cleanup

  pageMode = "planner"; // "analysis" | "help"

  analysisMode = "planner";
  poiMode = "enabled";

  constructor(private mapService: MapService) {
  }

  ngOnInit(): void {
    this.mapService.poiClickedObserver.subscribe(poiId => {
      this.pageMode = "poi";
    });
  }

  isDefault(): boolean {
    return !(this.isNodeSelected() || this.isRouteSelected());
  }

  isNodeSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "node";
  }

  isRouteSelected(): boolean {
    return this.selectedFeature && this.selectedFeature.featureType === "route";
  }

  pageModeChanged(event: MatButtonToggleChange) {
    this.pageMode = event.value;
  }

}
