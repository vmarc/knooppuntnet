import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-map-sidebar-legend",
  changeDetection: ChangeDetectionStrategy.OnPush,
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
