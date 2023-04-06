import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-poi-group-restaurants',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group
      name="restaurants"
      title="Restaurants"
      i18n-title="@@poi.group.restaurants"
    >
      <kpn-poi-config poiId="bar" />
      <kpn-poi-config poiId="bbq" />
      <kpn-poi-config poiId="biergarten" />
      <kpn-poi-config poiId="cafe" />
      <kpn-poi-config poiId="fastfood" />
      <kpn-poi-config poiId="foodcourt" />
      <kpn-poi-config poiId="icecream" />
      <kpn-poi-config poiId="pub" />
      <kpn-poi-config poiId="restaurant" />
    </kpn-poi-group>
  `,
})
export class PoiGroupRestaurantsComponent {}
