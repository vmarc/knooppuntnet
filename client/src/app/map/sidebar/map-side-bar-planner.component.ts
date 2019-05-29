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
  `
})
export class MapSidebarPlannerComponent {
}
