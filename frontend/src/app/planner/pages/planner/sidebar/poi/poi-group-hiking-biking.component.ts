import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-hiking-biking',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="hiking-biking" title="Hiking/biking" i18n-title="@@poi.group.hiking-biking">
      <ui-poi-config poiId="ebike-charging" />
      <ui-poi-config poiId="bicycle" />
      <ui-poi-config poiId="bicycle-rental" />
      <ui-poi-config poiId="bicycle-rental-2" />
      <ui-poi-config poiId="bicycle-parking" />
      <ui-poi-config poiId="information" />
      <ui-poi-config poiId="bench" />
      <ui-poi-config poiId="picnic" />
      <ui-poi-config poiId="toilets" />
      <ui-poi-config poiId="drinking-water" />
      <ui-poi-config poiId="themepark" />
      <ui-poi-config poiId="viewpoint" />
      <ui-poi-config poiId="attraction" />
      <ui-poi-config poiId="defibrillator" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupHikingBikingComponent {}
