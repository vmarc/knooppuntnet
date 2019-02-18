import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-shops',
  template: `
    <kpn-poi-group name="Shops" i18n-name="@@poi.group.shops">
      <kpn-poi-config poiId="beauty"></kpn-poi-config>
      <kpn-poi-config poiId="bicycle"></kpn-poi-config>
      <kpn-poi-config poiId="books_stationary"></kpn-poi-config>
      <kpn-poi-config poiId="car"></kpn-poi-config>
      <kpn-poi-config poiId="chemist"></kpn-poi-config>
      <kpn-poi-config poiId="clothes"></kpn-poi-config>
      <kpn-poi-config poiId="copyshop"></kpn-poi-config>
      <kpn-poi-config poiId="cosmetics"></kpn-poi-config>
      <kpn-poi-config poiId="department_store"></kpn-poi-config>
      <kpn-poi-config poiId="diy_hardware"></kpn-poi-config>
      <kpn-poi-config poiId="garden_centre"></kpn-poi-config>
      <kpn-poi-config poiId="general"></kpn-poi-config>
      <kpn-poi-config poiId="gift"></kpn-poi-config>
      <kpn-poi-config poiId="hairdresser"></kpn-poi-config>
      <kpn-poi-config poiId="jewelry"></kpn-poi-config>
      <kpn-poi-config poiId="kiosk"></kpn-poi-config>
      <kpn-poi-config poiId="leather"></kpn-poi-config>
      <kpn-poi-config poiId="marketplace"></kpn-poi-config>
      <kpn-poi-config poiId="musical_instrument"></kpn-poi-config>
      <kpn-poi-config poiId="optician"></kpn-poi-config>
      <kpn-poi-config poiId="pets"></kpn-poi-config>
      <kpn-poi-config poiId="phone"></kpn-poi-config>
      <kpn-poi-config poiId="photo"></kpn-poi-config>
      <kpn-poi-config poiId="shoes"></kpn-poi-config>
      <kpn-poi-config poiId="shopping_centre"></kpn-poi-config>
      <kpn-poi-config poiId="textiles"></kpn-poi-config>
      <kpn-poi-config poiId="toys"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupShopsComponent {
}
