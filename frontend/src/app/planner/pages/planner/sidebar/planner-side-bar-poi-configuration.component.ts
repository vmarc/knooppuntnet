import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { MapPoiConfigComponent } from './poi/map-poi-config.component';

@Component({
  selector: 'ui-planner-sidebar-poi-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-collapse>
      <nz-collapse-panel [nzHeader]="poiHeader" [nzActive]="false">
        <ng-template #poiHeader>
          <span i18n="@@planner.pois.title">Points of interest</span>
          &nbsp;&nbsp;
          <span class="kpn-thin" i18n="@@planner.pois.enabled-disabled">(Enabled/Disabled)</span>
        </ng-template>

        <label
          nz-checkbox
          [nzChecked]="isEnabled()"
          (nzCheckedChange)="enabledChanged($event)"
          i18n="@@planner.pois.enabled"
        >
          Show points of interest on the map
        </label>

        <!-- TODO show warning only when zoom level not high enough to see the icons on the map -->
        <p>
          <i i18n="@@planner.pois.zoom-in"> Zoom in to see the icons on the map. </i>
        </p>

        <ui-map-poi-config />
        <button nz-button i18n="@@planner.pois.reset">Reset configuration to default</button>
      </nz-collapse-panel>
    </nz-collapse>
  `,
  imports: [
    MapPoiConfigComponent,
    NzButtonComponent,
    NzCheckboxComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
  ],
})
export class PlannerSideBarPoiConfigurationComponent {
  private readonly poiService = inject(OldPoiService);

  enabledChanged(checked: boolean) {
    this.poiService.updateEnabled(checked);
  }

  isEnabled() {
    return this.poiService.isEnabled();
  }
}
