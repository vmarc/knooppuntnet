import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-foodshops',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="foodshops" title="Foodshops" i18n-title="@@poi.group.foodshops">
      <ui-poi-config poiId="alcohol" />
      <ui-poi-config poiId="bakery" />
      <ui-poi-config poiId="beverages" />
      <ui-poi-config poiId="butcher" />
      <ui-poi-config poiId="cheese" />
      <ui-poi-config poiId="chocolate" />
      <ui-poi-config poiId="confectionery" />
      <ui-poi-config poiId="coffee" />
      <ui-poi-config poiId="dairy" />
      <ui-poi-config poiId="deli" />
      <ui-poi-config poiId="grocery" />
      <ui-poi-config poiId="organic" />
      <ui-poi-config poiId="seafood" />
      <ui-poi-config poiId="supermarket" />
      <ui-poi-config poiId="wine" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupFoodshopsComponent {}
