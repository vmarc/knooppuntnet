import {Component} from "@angular/core";

@Component({
  selector: "kpn-map-sidebar-legend",
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header i18n="@@planner.legend">
        Legend
      </mat-expansion-panel-header>
      <pre>
          -----
          
          +++++
          
          =====
          
          -*-*-*
        </pre>
    </mat-expansion-panel>
  `
})
export class MapSidebarLegendComponent {
}
