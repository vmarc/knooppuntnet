import {Component} from '@angular/core';

@Component({
  selector: 'kpn-map-sidebar-poi-configuration',
  template: `
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
  `
})
export class MapSidebarPoiConfigurationComponent {
}
