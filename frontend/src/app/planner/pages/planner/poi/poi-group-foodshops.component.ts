import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'kpn-poi-group-foodshops',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group name="foodshops" title="Foodshops" i18n-title="@@poi.group.foodshops">
      <kpn-poi-config poiId="alcohol" />
      <kpn-poi-config poiId="bakery" />
      <kpn-poi-config poiId="beverages" />
      <kpn-poi-config poiId="butcher" />
      <kpn-poi-config poiId="cheese" />
      <kpn-poi-config poiId="chocolate" />
      <kpn-poi-config poiId="confectionery" />
      <kpn-poi-config poiId="coffee" />
      <kpn-poi-config poiId="dairy" />
      <kpn-poi-config poiId="deli" />
      <kpn-poi-config poiId="grocery" />
      <kpn-poi-config poiId="organic" />
      <kpn-poi-config poiId="seafood" />
      <kpn-poi-config poiId="supermarket" />
      <kpn-poi-config poiId="wine" />
    </kpn-poi-group>
  `,
  standalone: true,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupFoodshopsComponent {}
