import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-food-shops',
  template: `
    <kpn-poi-group name="Food shops" i18n-name="@@poi.group.food-shops">
      <kpn-poi-config poiId="alcohol"></kpn-poi-config>
      <kpn-poi-config poiId="bakery"></kpn-poi-config>
      <kpn-poi-config poiId="beverages"></kpn-poi-config>
      <kpn-poi-config poiId="butcher"></kpn-poi-config>
      <kpn-poi-config poiId="cheese"></kpn-poi-config>
      <kpn-poi-config poiId="chocolate"></kpn-poi-config>
      <kpn-poi-config poiId="confectionery"></kpn-poi-config>
      <kpn-poi-config poiId="coffee"></kpn-poi-config>
      <kpn-poi-config poiId="dairy"></kpn-poi-config>
      <kpn-poi-config poiId="deli"></kpn-poi-config>
      <kpn-poi-config poiId="drinking_water"></kpn-poi-config>
      <kpn-poi-config poiId="grocery"></kpn-poi-config>
      <kpn-poi-config poiId="organic"></kpn-poi-config>
      <kpn-poi-config poiId="seafood"></kpn-poi-config>
      <kpn-poi-config poiId="supermarket"></kpn-poi-config>
      <kpn-poi-config poiId="wine"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupFoodShopsComponent {
}
