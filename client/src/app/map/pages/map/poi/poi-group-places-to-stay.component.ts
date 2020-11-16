import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';

@Component({
  selector: 'kpn-poi-group-places-to-stay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group name="places-to-stay" title="Places to stay" i18n-title="@@poi.group.places-to-stay">
      <kpn-poi-config poiId="alpine-hut"></kpn-poi-config>
      <kpn-poi-config poiId="apartment"></kpn-poi-config>
      <kpn-poi-config poiId="campsite"></kpn-poi-config>
      <kpn-poi-config poiId="chalet"></kpn-poi-config>
      <kpn-poi-config poiId="guesthouse"></kpn-poi-config>
      <kpn-poi-config poiId="hostel"></kpn-poi-config>
      <kpn-poi-config poiId="hotel"></kpn-poi-config>
      <kpn-poi-config poiId="motel"></kpn-poi-config>
      <!--<kpn-poi-config poiId="spa"></kpn-poi-config>-->
      <kpn-poi-config poiId="sauna"></kpn-poi-config>
    </kpn-poi-group>
  `
})
export class PoiGroupPlacesToStayComponent {
}
