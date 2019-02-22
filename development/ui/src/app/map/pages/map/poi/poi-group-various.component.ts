import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-various',
  template: `
    <kpn-poi-group name="various" title="Various" i18n-title="@@poi.group.various">
      <kpn-poi-config poiId="bus_stop"></kpn-poi-config>
      <kpn-poi-config poiId="ebike_charging"></kpn-poi-config>
      <kpn-poi-config poiId="travel_agency"></kpn-poi-config>
      <kpn-poi-config poiId="defibrillator"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupVariousComponent {
}
