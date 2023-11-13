import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'kpn-poi-group-places-to-stay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group
      name="places-to-stay"
      title="Places to stay"
      i18n-title="@@poi.group.places-to-stay"
    >
      <kpn-poi-config poiId="alpine-hut" />
      <kpn-poi-config poiId="apartment" />
      <kpn-poi-config poiId="campsite" />
      <kpn-poi-config poiId="chalet" />
      <kpn-poi-config poiId="guesthouse" />
      <kpn-poi-config poiId="hostel" />
      <kpn-poi-config poiId="hotel" />
      <kpn-poi-config poiId="motel" />
      <!--<kpn-poi-config poiId="spa"></kpn-poi-config>-->
      <kpn-poi-config poiId="sauna" />
    </kpn-poi-group>
  `,
  standalone: true,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupPlacesToStayComponent {}
