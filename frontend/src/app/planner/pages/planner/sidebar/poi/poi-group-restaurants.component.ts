import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-restaurants',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="restaurants" title="Restaurants" i18n-title="@@poi.group.restaurants">
      <ui-poi-config poiId="bar" />
      <ui-poi-config poiId="bbq" />
      <ui-poi-config poiId="biergarten" />
      <ui-poi-config poiId="cafe" />
      <ui-poi-config poiId="fastfood" />
      <ui-poi-config poiId="foodcourt" />
      <ui-poi-config poiId="icecream" />
      <ui-poi-config poiId="pub" />
      <ui-poi-config poiId="restaurant" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupRestaurantsComponent {}
