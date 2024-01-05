import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { PoiService } from '@app/services';
import { MapPoiConfigComponent } from '../poi/map-poi-config.component';

@Component({
  selector: 'kpn-planner-sidebar-poi-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        <span i18n="@@planner.pois.title">Points of interest</span>
        &nbsp;&nbsp;
        <span class="kpn-thin" i18n="@@planner.pois.enabled-disabled">(Enabled/Disabled)</span>
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
          <i i18n="@@planner.pois.zoom-in"> Zoom in to see the icons on the map. </i>
        </p>

        <kpn-map-poi-config />
        <button mat-stroked-button i18n="@@planner.pois.reset">
          Reset configuration to default
        </button>
      </ng-template>
    </mat-expansion-panel>
  `,
  standalone: true,
  imports: [MapPoiConfigComponent, MatButtonModule, MatCheckboxModule, MatExpansionModule],
})
export class PlannerSideBarPoiConfigurationComponent {
  private readonly poiService = inject(PoiService);

  enabledChanged(event: MatCheckboxChange) {
    this.poiService.updateEnabled(event.checked);
  }

  isEnabled() {
    return this.poiService.isEnabled();
  }
}
