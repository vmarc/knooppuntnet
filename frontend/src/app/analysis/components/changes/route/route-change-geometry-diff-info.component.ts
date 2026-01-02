import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { GeometryDiff } from '@api/common/route/geometry-diff';
import { RouteChangeGeometryDiffInfoDetailComponent } from './route-change-geometry-diff-info-detail.component';

@Component({
  selector: 'ui-route-change-geometry-diff-info',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let info = geometryDiff().info;
    <ui-route-change-geometry-diff-info-detail
      title="Added"
      [detail]="info.added"
      color="rgb(64,255,0)"
    />
    <ui-route-change-geometry-diff-info-detail
      title="Removed"
      [detail]="info.removed"
      color="red"
    />
    <ui-route-change-geometry-diff-info-detail
      title="Unchanged"
      [detail]="info.common"
      color="blue"
    />
  `,
  imports: [RouteChangeGeometryDiffInfoDetailComponent],
})
export class RouteChangeGeometryDiffInfoComponent {
  readonly geometryDiff = input.required<GeometryDiff>();
}
