import {Component} from "@angular/core";

@Component({
  selector: "kpn-map-sidebar-planner",
  template: `

    <mat-expansion-panel [expanded]="true">
      <mat-expansion-panel-header>
        <mat-panel-title>
          <h1>Route planner</h1>
        </mat-panel-title>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <kpn-plan></kpn-plan>
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

    <mat-expansion-panel>
      <mat-expansion-panel-header>
        <mat-panel-title>
          Step 3
        </mat-panel-title>
        <mat-panel-description>
          Route ready
        </mat-panel-description>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <button mat-stroked-button>Print</button>
        <button mat-stroked-button>GPX</button>
      </ng-template>
    </mat-expansion-panel>
  `
})
export class MapSidebarPlannerComponent {
}
