import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-amenity',
  template: `
    <kpn-poi-group name="amenity" title="Amenity" i18n-title="@@poi.group.amenity">
      <kpn-poi-config poiId="atm"></kpn-poi-config>
      <kpn-poi-config poiId="bank"></kpn-poi-config>
      <kpn-poi-config poiId="cinema"></kpn-poi-config>
      <kpn-poi-config poiId="clinic"></kpn-poi-config>
      <kpn-poi-config poiId="embassy"></kpn-poi-config>
      <kpn-poi-config poiId="firestation"></kpn-poi-config>
      <kpn-poi-config poiId="fuel"></kpn-poi-config>
      <kpn-poi-config poiId="hospital"></kpn-poi-config>
      <kpn-poi-config poiId="library"></kpn-poi-config>
      <kpn-poi-config poiId="musicschool"></kpn-poi-config>
      <kpn-poi-config poiId="parking"></kpn-poi-config>
      <kpn-poi-config poiId="pharmacy"></kpn-poi-config>
      <kpn-poi-config poiId="police"></kpn-poi-config>
      <kpn-poi-config poiId="postbox"></kpn-poi-config>
      <kpn-poi-config poiId="postoffice"></kpn-poi-config>
      <!--    <kpn-poi-config poiId="school_college"></kpn-poi-config > -->
      <kpn-poi-config poiId="taxi"></kpn-poi-config>
      <kpn-poi-config poiId="theatre"></kpn-poi-config>
      <kpn-poi-config poiId="university"></kpn-poi-config>
      <kpn-poi-config poiId="cemetery"></kpn-poi-config>
      <kpn-poi-config poiId="busstop"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupAmenityComponent {
}
