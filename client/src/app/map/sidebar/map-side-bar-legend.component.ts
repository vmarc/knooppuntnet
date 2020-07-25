import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {MapService} from "../../components/ol/services/map.service";

/* tslint:disable:template-i18n work-in-progress */
@Component({
  selector: "kpn-map-sidebar-legend",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="true">
      <mat-expansion-panel-header i18n="@@planner.legend">
        Legend
      </mat-expansion-panel-header>

      <div *ngIf="mapService.mapMode$ | async as mapMode">
        <div *ngIf="mapMode === 'surface'" class="legend">
          <div>
            <kpn-legend-icon color="rgb(0, 200, 0)"></kpn-legend-icon>
            <span>Paved</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(255, 165, 0)"></kpn-legend-icon>
            <span>Unpaved</span>
          </div>
        </div>
        <div *ngIf="mapMode === 'survey'" class="legend">
          <div class="title">
            Most recent survey
          </div>
          <div>
            <kpn-legend-icon color="rgb(0, 255, 0)"></kpn-legend-icon>
            <span>Last month</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(0, 200, 0)"></kpn-legend-icon>
            <span>Last half year</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(0, 150, 0)"></kpn-legend-icon>
            <span>Last year</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(0, 90, 0)"></kpn-legend-icon>
            <span>Last two years</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(150, 0, 0)"></kpn-legend-icon>
            <span>More than two years ago</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(200, 200, 200)"></kpn-legend-icon>
            <span>Unknown</span>
          </div>
        </div>
        <div *ngIf="mapMode === 'analysis'" class="legend">
          <div>
            <kpn-legend-icon color="rgb(0, 200, 0)"></kpn-legend-icon>
            <span>TODO</span>
          </div>
          <div>
            <kpn-legend-icon color="rgb(150, 0, 0)"></kpn-legend-icon>
            <span>TODO</span>
          </div>
          <div>
            <span>etc.</span>
          </div>
        </div>

        <div class="legend">
          <div>
            <div class="legend-icon">
              <img src="/assets/images/marker-icon-blue.png" class="image" alt="Start node icon">
            </div>
            <span>Start node</span>
          </div>
          <div>
            <div class="legend-icon">
              <img src="/assets/images/marker-icon-green.png" class="image" alt="End node icon">
            </div>
            <span>End node</span>
          </div>
          <div>
            <div class="legend-icon">
              <img src="/assets/images/marker-icon-orange.png" class="image" alt="Via node icon">
            </div>
            <span>Via node</span>
          </div>
        </div>
      </div>

    </mat-expansion-panel>
  `,
  styles: [`
    .legend > div {
      display: flex;
      align-items: center;
    }

    .title {
      padding-bottom: 15px;
    }

    .legend-icon {
      width: 60px;
      padding-right: 10px;
      text-align: center;
    }
  `]
})
export class MapSidebarLegendComponent {
  constructor(public mapService: MapService) {
  }
}
