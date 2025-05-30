import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-places-to-stay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group
      name="places-to-stay"
      title="Places to stay"
      i18n-title="@@poi.group.places-to-stay"
    >
      <ui-poi-config poiId="alpine-hut" />
      <ui-poi-config poiId="apartment" />
      <ui-poi-config poiId="campsite" />
      <ui-poi-config poiId="chalet" />
      <ui-poi-config poiId="guesthouse" />
      <ui-poi-config poiId="hostel" />
      <ui-poi-config poiId="hotel" />
      <ui-poi-config poiId="motel" />
      <!--<ui-poi-config poiId="spa"></ui-poi-config>-->
      <ui-poi-config poiId="sauna" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupPlacesToStayComponent {}
