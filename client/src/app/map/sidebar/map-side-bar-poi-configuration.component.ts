import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { PoiService } from '../../services/poi.service';

@Component({
  selector: 'kpn-map-sidebar-poi-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        <span i18n="@@planner.pois.title">Points of interest</span>
        &nbsp;&nbsp;
        <span class="kpn-thin" i18n="@@planner.pois.enabled-disabled"
          >(Enabled/Disabled)</span
        >
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-checkbox
          [checked]="isEnabled()"
          (change)="enabledChanged($event)"
          i18n="@@planner.pois.enabled"
        >
          Show points of interest on the map
        </mat-checkbox>

        <!-- TODO show warning only when zoom level not high enough to see the icons on the map -->
        <p>
          <i i18n="@@planner.pois.zoom-in">
            Zoom in to see the icons on the map.
          </i>
        </p>

        <kpn-map-poi-config></kpn-map-poi-config>
        <button mat-stroked-button i18n="@@planner.pois.reset">
          Reset configuration to default
        </button>
      </ng-template>
    </mat-expansion-panel>
  `,
})
export class MapSidebarPoiConfigurationComponent {
  constructor(private poiService: PoiService) {}

  enabledChanged(event: MatCheckboxChange) {
    this.poiService.updateEnabled(event.checked);
  }

  isEnabled() {
    return this.poiService.isEnabled();
  }
}
