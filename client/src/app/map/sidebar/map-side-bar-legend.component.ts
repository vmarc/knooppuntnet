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

      <div class="legend">
        <div>
          <kpn-legend-icon color="rgb(0,200,0)"></kpn-legend-icon>
          <span>TODO</span>
        </div>
        <div>
          <kpn-legend-icon color="rgb(150,0,0)"></kpn-legend-icon>
          <span>TODO</span>
        </div>
        <div>
          <span>etc.</span>
        </div>
      </div>

    </mat-expansion-panel>
  `,
  styles: [`
    .legend > div {
      display: flex;
      align-items: center;
    }
  `]
})
export class MapSidebarLegendComponent {
}
