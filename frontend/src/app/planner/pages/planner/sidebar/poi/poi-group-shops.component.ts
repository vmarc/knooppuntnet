import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-shops',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="shops" title="Shops" i18n-title="@@poi.group.shops">
      <ui-poi-config poiId="beauty" />
      <ui-poi-config poiId="books-stationary" />
      <ui-poi-config poiId="car" />
      <ui-poi-config poiId="chemist" />
      <ui-poi-config poiId="clothes" />
      <ui-poi-config poiId="copyshop" />
      <ui-poi-config poiId="cosmetics" />
      <ui-poi-config poiId="departmentstore" />
      <ui-poi-config poiId="diy-hardware" />
      <ui-poi-config poiId="garden-centre" />
      <ui-poi-config poiId="general" />
      <ui-poi-config poiId="gift" />
      <ui-poi-config poiId="hairdresser" />
      <ui-poi-config poiId="jewelry" />
      <ui-poi-config poiId="kiosk" />
      <ui-poi-config poiId="leather" />
      <ui-poi-config poiId="marketplace" />
      <ui-poi-config poiId="musical-instrument" />
      <ui-poi-config poiId="optician" />
      <ui-poi-config poiId="pets" />
      <ui-poi-config poiId="phone" />
      <ui-poi-config poiId="photo" />
      <ui-poi-config poiId="shoes" />
      <ui-poi-config poiId="shoppingcentre" />
      <ui-poi-config poiId="textiles" />
      <ui-poi-config poiId="toys" />
      <ui-poi-config poiId="travelagency" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupShopsComponent {}
