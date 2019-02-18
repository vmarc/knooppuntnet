import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-restaurants',
  template: `
    <kpn-poi-group name="Restaurants" i18n-name="@@poi.group.restaurants">
      <kpn-poi-config poiId="bar"></kpn-poi-config>
      <kpn-poi-config poiId="bbq"></kpn-poi-config>
      <kpn-poi-config poiId="biergarten"></kpn-poi-config>
      <kpn-poi-config poiId="cafe"></kpn-poi-config>
      <kpn-poi-config poiId="fast_food"></kpn-poi-config>
      <kpn-poi-config poiId="food_court"></kpn-poi-config>
      <kpn-poi-config poiId="ice_cream"></kpn-poi-config>
      <kpn-poi-config poiId="pub"></kpn-poi-config>
      <kpn-poi-config poiId="restaurant"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupRestaurantsComponent {
}
