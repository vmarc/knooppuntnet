import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SegmentInfo } from '@api/common/route/segment-info';
import { EditService } from '@app/shared/components/edit.service';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';

@Component({
  selector: 'ui-route-segment-popup',
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
export class RouteSegmentPopupComponent {
  private readonly editService = inject(EditService);

  readonly segment = input.required<SegmentInfo>();

  josmZoomToSelectedDeviation(): void {
    const bounds = this.segment()?.bounds;
    if (bounds) {
      this.editService.edit({ bounds: bounds });
    }
  }

  zoomToDeviationInOpenstreetMap(): void {
    const bounds = this.segment()?.bounds;
    if (bounds) {
      const url = `https://www.openstreetmap.org/?bbox=${bounds.minLon},${bounds.minLat},${bounds.maxLon},${bounds.maxLat}`;
      window.open(url, 'openstreetmap');
    }
  }
}
