import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-poi-group-amenity',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group
      name="amenity"
      title="Amenity"
      i18n-title="@@poi.group.amenity"
    >
      <kpn-poi-config poiId="atm"/>
      <kpn-poi-config poiId="bank"/>
      <kpn-poi-config poiId="cinema"/>
      <kpn-poi-config poiId="clinic"/>
      <kpn-poi-config poiId="embassy"/>
      <kpn-poi-config poiId="firestation"/>
      <kpn-poi-config poiId="fuel"/>
      <kpn-poi-config poiId="hospital"/>
      <kpn-poi-config poiId="library"/>
      <kpn-poi-config poiId="musicschool"/>
      <kpn-poi-config poiId="parking"/>
      <kpn-poi-config poiId="pharmacy"/>
      <kpn-poi-config poiId="police"/>
      <kpn-poi-config poiId="postbox"/>
      <kpn-poi-config poiId="postoffice"/>
      <!--    <kpn-poi-config poiId="school_college"></kpn-poi-config > -->
      <kpn-poi-config poiId="taxi"/>
      <kpn-poi-config poiId="theatre"/>
      <kpn-poi-config poiId="university"/>
      <kpn-poi-config poiId="cemetery"/>
      <kpn-poi-config poiId="busstop"/>
    </kpn-poi-group>
  `,
})
export class PoiGroupAmenityComponent {}
