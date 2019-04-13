import {Component} from "@angular/core";
import {MatCheckboxChange} from "@angular/material";
import {PoiService} from "../../poi.service";

@Component({
  selector: "kpn-map-sidebar-poi-configuration",
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        Points of interest<span class="kpn-thin">&nbsp;&nbsp;(Enabled/Disabled)</span>
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-checkbox [checked]="isEnabled()" (change)="enabledChanged($event)">Show points of interest on the map</mat-checkbox>

        <!-- TODO show warning only when zoom level not high enough to see the icons on the map -->
        <p>
          <i>
            Zoom in to see the icons on the map.
          </i>
        </p>

        <kpn-map-poi-config></kpn-map-poi-config>
        <button mat-stroked-button>Reset configuration to default</button>

      </ng-template>
    </mat-expansion-panel>
  `
})
export class MapSidebarPoiConfigurationComponent {

  constructor(private poiService: PoiService) {
  }

  enabledChanged(event: MatCheckboxChange) {
    this.poiService.updateEnabled(event.checked);
  }

  isEnabled() {
    return this.poiService.isEnabled();
  }

}
