import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-places-to-stay',
  template: `
    <kpn-poi-group name="places_to_stay" title="Places to stay" i18n-title="@@poi.group.places-to-stay">
      <kpn-poi-config poiId="alpine_hut"></kpn-poi-config>
      <kpn-poi-config poiId="apartment"></kpn-poi-config>
      <kpn-poi-config poiId="camp_site"></kpn-poi-config>
      <kpn-poi-config poiId="chalet"></kpn-poi-config>
      <kpn-poi-config poiId="guest_house"></kpn-poi-config>
      <kpn-poi-config poiId="hostel"></kpn-poi-config>
      <kpn-poi-config poiId="hotel"></kpn-poi-config>
      <kpn-poi-config poiId="motel"></kpn-poi-config>
      <!--<kpn-poi-config poiId="spa"></kpn-poi-config>-->
      <kpn-poi-config poiId="sauna"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupPlacesToStayComponent {
}
