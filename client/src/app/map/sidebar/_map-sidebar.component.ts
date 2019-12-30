import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from "@angular/core";
import {MatButtonToggleChange} from "@angular/material";
import {MapService} from "src/app/components/ol/map.service";
import {SelectedFeature} from "../../components/ol/domain/selected-feature";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {Subscriptions} from "../../util/Subscriptions";

@Component({
  selector: "kpn-map-sidebar",
  template: `
    <div *ngIf="!!networkType">
      <div>
        <mat-button-toggle-group [value]="pageMode" (change)="pageModeChanged($event)">
          <mat-button-toggle value="planner">
            <mat-icon svgIcon="map"></mat-icon>
            <span i18n="@@planner.menu.planner">Planner</span>
          </mat-button-toggle>
          <mat-button-toggle value="analysis">
            <mat-icon svgIcon="analysis"></mat-icon>
            <span i18n="@@planner.menu.analysis">Analysis</span>
          </mat-button-toggle>
          <mat-button-toggle value="poi">
            <mat-icon svgIcon="help"></mat-icon>
            <span i18n="@@planner.menu.poi">POI</span>
          </mat-button-toggle>
        </mat-button-toggle-group>
      </div>

      <div *ngIf="pageMode === 'planner'">
        <kpn-map-sidebar-planner></kpn-map-sidebar-planner>
      </div>

      <kpn-map-sidebar-analysis *ngIf="pageMode === 'analysis'" [networkType]="networkType"></kpn-map-sidebar-analysis>

      <ng-container *ngIf="pageMode !== 'poi'">
        <mat-divider></mat-divider>
        <kpn-map-sidebar-appearance></kpn-map-sidebar-appearance>
        <kpn-map-sidebar-legend></kpn-map-sidebar-legend>
        <kpn-map-sidebar-poi-configuration></kpn-map-sidebar-poi-configuration>
      </ng-container>
    </div>
  `
})
export class MapSidebarComponent implements OnInit, OnDestroy {

  private readonly subscriptions = new Subscriptions();

  @Input() selectedFeature: SelectedFeature;

  pageMode = "planner"; // "analysis" | "help"

  networkType: NetworkType = null;

  constructor(private mapService: MapService,
              private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.subscriptions.add(this.mapService.networkType.subscribe(networkType => {
      this.networkType = networkType;
      this.cdr.detectChanges();
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
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
