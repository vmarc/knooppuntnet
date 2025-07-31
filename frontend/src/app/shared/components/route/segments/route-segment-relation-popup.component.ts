import { inject } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SegmentInfo } from '@api/common/route/segment-info';
import { EditService } from '@app/shared/components/edit.service';
import { NzMenuDividerDirective } from 'ng-zorro-antd/menu';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';

@Component({
  selector: 'ui-route-segment-relation-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul nz-menu>
      <li
        nz-menu-item
        (click)="josmZoomToSelectedDeviation()"
        i18n="@@monitor.route.segments.zoom-josm"
      >
        Highlight relation in map
      </li>
      <li nz-menu-divider></li>
      <li
        nz-menu-item
        (click)="josmZoomToSelectedDeviation()"
        i18n="@@monitor.route.segments.zoom-josm"
      >
        Go here in JOSM
      </li>
      <li
        nz-menu-item
        (click)="zoomToDeviationInOpenstreetMap()"
        i18n="@@monitor.route.segments.zoom-openstreetmap"
      >
        Go here in openstreetmap.org
      </li>
    </ul>
  `,
  imports: [NzMenuDirective, NzMenuItemComponent, NzMenuDividerDirective],
})
export class RouteSegmentRelationPopupComponent {
  private readonly editService = inject(EditService);

  readonly segment = input.required<SegmentInfo>();
  readonly relationId = input.required<number>();

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
