import { DOCUMENT } from '@angular/core';
import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MonitorRouteDeviationInfo } from '@api/common/monitor/monitor-route-deviation-info';
import { EditService } from '@app/shared/components/edit.service';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';

@Component({
  selector: 'ui-monitor-route-deviation-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul nz-menu>
      <li
        nz-menu-item
        (click)="josmZoomToSelectedDeviation()"
        i18n="@@monitor.route.deviations.zoom-josm"
      >
        Go here in JOSM
      </li>
      <li
        nz-menu-item
        (click)="zoomToDeviationInOpenstreetMap()"
        i18n="@@monitor.route.deviations.zoom-openstreetmap"
      >
        Go here in openstreetmap.org
      </li>
    </ul>
  `,
  imports: [NzMenuDirective, NzMenuItemComponent],
})
export class MonitorRouteDeviationPopupComponent {
  private readonly editService = inject(EditService);
  private readonly document = inject(DOCUMENT);
  private readonly window = this.document?.defaultView;
  readonly deviation = input.required<MonitorRouteDeviationInfo>();

  josmZoomToSelectedDeviation(): void {
    const bounds = this.deviation()?.bounds;
    if (bounds) {
      this.editService.edit({ bounds: bounds });
    }
  }

  zoomToDeviationInOpenstreetMap(): void {
    const bounds = this.deviation()?.bounds;
    if (bounds) {
      const url = `https://www.openstreetmap.org/?bbox=${bounds.minLon},${bounds.minLat},${bounds.maxLon},${bounds.maxLat}`;
      this.window.open(url, 'openstreetmap');
    }
  }
}
