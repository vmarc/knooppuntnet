import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-landmarks',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="landmarks" title="Landmarks" i18n-title="@@poi.group.landmarks">
      <ui-poi-config poiId="windmill" />
      <ui-poi-config poiId="watermill" />
      <ui-poi-config poiId="place-of-worship" />
      <ui-poi-config poiId="church" />
      <ui-poi-config poiId="mosque" />
      <ui-poi-config poiId="buddhist-temple" />
      <ui-poi-config poiId="hindu-temple" />
      <ui-poi-config poiId="synagogue" />
      <ui-poi-config poiId="wayside-shrine" />
      <ui-poi-config poiId="heritage" />
      <ui-poi-config poiId="historic" />
      <ui-poi-config poiId="boundary-stone" />
      <ui-poi-config poiId="castle" />
      <ui-poi-config poiId="monument-memorial" />
      <ui-poi-config poiId="statue" />
      <ui-poi-config poiId="zoo" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupLandmarksComponent {}
