import {Component, Input} from '@angular/core';
import {NetworkType} from "../../kpn/shared/network-type";
import {SelectedFeature} from "../../map/domain/selected-feature";
import {MatButtonToggleChange} from "@angular/material";

@Component({
  selector: 'kpn-planner-sidebar',
  template: `

    <kpn-network-type-selector></kpn-network-type-selector>

    <div>
      <mat-button-toggle-group [value]="pageMode" (change)="pageModeChanged($event)">
        <mat-button-toggle value="planner">
          <mat-icon svgIcon="planner"></mat-icon>
          Planner
        </mat-button-toggle>
        <mat-button-toggle value="analysis">
          <mat-icon svgIcon="analysis"></mat-icon>
          Analysis
        </mat-button-toggle>
        <mat-button-toggle value="help">
          <mat-icon svgIcon="help"></mat-icon>
          Help
        </mat-button-toggle>
      </mat-button-toggle-group>
    </div>

    <div *ngIf="pageMode === 'planner'">
      <mat-expansion-panel [expanded]="true">
        <mat-expansion-panel-header>
          <mat-panel-title>
            <h1>Route planner</h1>
          </mat-panel-title>
        </mat-expansion-panel-header>
        <ng-template matExpansionPanelContent>
          <p>
            This is about how to use the route planner.
          </p>
          <p>
            Zoom in ...
          </p>
          <p>
            bb bbb bbb
          </p>
        </ng-template>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          <mat-panel-title>
            Step 1
          </mat-panel-title>
          <mat-panel-description>
            Select starting point
          </mat-panel-description>
        </mat-expansion-panel-header>
        <ng-template matExpansionPanelContent>
          <p>
            bla bla
          </p>
          <p>
            bb bbb bbb
          </p>
        </ng-template>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          <mat-panel-title>
            Step 2
          </mat-panel-title>
          <mat-panel-description>
            Select end point
          </mat-panel-description>
        </mat-expansion-panel-header>
        <ng-template matExpansionPanelContent>
          <p>
            bla bla
          </p>
          <p>
            bb bbb bbb
          </p>
        </ng-template>
      </mat-expansion-panel>
    </div>


    <mat-expansion-panel *ngIf="pageMode === 'analysis'" [expanded]="true">
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
          [nodeId]="selectedFeature.featureId"
          [nodeName]="selectedFeature.name"
          [networkType]="networkType">
        </kpn-map-detail-node>

        <kpn-map-detail-route
          *ngIf="isRouteSelected()"
          [routeId]="selectedFeature.featureId"
          [routeName]="selectedFeature.name">
        </kpn-map-detail-route>

      </ng-template>
    </mat-expansion-panel>

    <div *ngIf="pageMode === 'help'">
      <h1>Help</h1>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          How to navigate the map?
        </mat-expansion-panel-header>
        <p>
          Explanation about how to navigate the map?
        </p>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          How to plan a trip?
        </mat-expansion-panel-header>
        <p>
          Explanation about how to plan a trip?
        </p>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          How to add points of interest to the map?
        </mat-expansion-panel-header>
        <p>
          Explanation about points of interest.
        </p>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          What is OpenStreetMap?
        </mat-expansion-panel-header>
        <p>
          Explanation about OpenstreetMap.
        </p>
      </mat-expansion-panel>
      <mat-expansion-panel>
        <mat-expansion-panel-header>
          How can I contribute?
        </mat-expansion-panel-header>
        <p>
          Explanation about how to contribute to the map.
        </p>
      </mat-expansion-panel>
    </div>





    <mat-divider></mat-divider>

    <mat-expansion-panel>
      <mat-expansion-panel-header>
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="analysisMode">
          <mat-radio-button value="planner" class="mode-radio-button">
            Surface
          </mat-radio-button>
          <mat-radio-button value="status" class="mode-radio-button">
            Node and route quality status
          </mat-radio-button>
          <mat-radio-button value="survey" class="mode-radio-button">
            Date last survey
          </mat-radio-button>
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>

    <mat-expansion-panel>
      <mat-expansion-panel-header>
        Legend
      </mat-expansion-panel-header>
      <pre>
          -----
          
          +++++
          
          =====
          
          -*-*-*
        </pre>
    </mat-expansion-panel>


    <mat-expansion-panel>
      <mat-expansion-panel-header>
        Points of interest<span class="kpn-thin">&nbsp;&nbsp;(Enabled/Disabled)</span>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-checkbox>Show points of interest on the map</mat-checkbox>

        <!-- show warning only when zoom level not high enough to see the icons on the map -->
        <p>
          <i>
            Zoom in to see the icons on the map.
          </i>
        </p>

        <kpn-map-poi-config></kpn-map-poi-config>
        <button mat-stroked-button>Reset configuration to default</button>

      </ng-template>
    </mat-expansion-panel>

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

  pageMode = "planner"; // "analysis" | "help"

  analysisMode = "planner";
  poiMode = "enabled";

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
