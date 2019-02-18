import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-sports',
  template: `
    <kpn-poi-group name="Sports" i18n-name="@@poi.group.sports">
      <kpn-poi-config poiId="american_football"></kpn-poi-config>
      <kpn-poi-config poiId="baseball"></kpn-poi-config>
      <kpn-poi-config poiId="basketball"></kpn-poi-config>
      <kpn-poi-config poiId="cycling"></kpn-poi-config>
      <kpn-poi-config poiId="gymnastics"></kpn-poi-config>
      <kpn-poi-config poiId="golf"></kpn-poi-config>
      <kpn-poi-config poiId="hockey"></kpn-poi-config>
      <kpn-poi-config poiId="horse_racing"></kpn-poi-config>
      <kpn-poi-config poiId="ice_hockey"></kpn-poi-config>
      <kpn-poi-config poiId="soccer"></kpn-poi-config>
      <kpn-poi-config poiId="sports_centre"></kpn-poi-config>
      <kpn-poi-config poiId="surfing"></kpn-poi-config>
      <kpn-poi-config poiId="swimming"></kpn-poi-config>
      <kpn-poi-config poiId="tennis"></kpn-poi-config>
      <kpn-poi-config poiId="volleyball"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupSportsComponent {
}
