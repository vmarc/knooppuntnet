import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-amenity',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="amenity" title="Amenity" i18n-title="@@poi.group.amenity">
      <ui-poi-config poiId="atm" />
      <ui-poi-config poiId="bank" />
      <ui-poi-config poiId="cinema" />
      <ui-poi-config poiId="clinic" />
      <ui-poi-config poiId="embassy" />
      <ui-poi-config poiId="firestation" />
      <ui-poi-config poiId="fuel" />
      <ui-poi-config poiId="hospital" />
      <ui-poi-config poiId="library" />
      <ui-poi-config poiId="musicschool" />
      <ui-poi-config poiId="parking" />
      <ui-poi-config poiId="pharmacy" />
      <ui-poi-config poiId="police" />
      <ui-poi-config poiId="postbox" />
      <ui-poi-config poiId="postoffice" />
      <!--    <ui-poi-config poiId="school_college"></ui-poi-config > -->
      <ui-poi-config poiId="taxi" />
      <ui-poi-config poiId="theatre" />
      <ui-poi-config poiId="university" />
      <ui-poi-config poiId="cemetery" />
      <ui-poi-config poiId="busstop" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupAmenityComponent {}
