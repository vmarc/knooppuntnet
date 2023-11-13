import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'kpn-poi-group-sports',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-poi-group name="sports" title="Sports" i18n-title="@@poi.group.sports">
      <kpn-poi-config poiId="american-football" />
      <kpn-poi-config poiId="baseball" />
      <kpn-poi-config poiId="basketball" />
      <kpn-poi-config poiId="cycling" />
      <kpn-poi-config poiId="gymnastics" />
      <kpn-poi-config poiId="golf" />
      <kpn-poi-config poiId="hockey" />
      <kpn-poi-config poiId="horseracing" />
      <kpn-poi-config poiId="icehockey" />
      <kpn-poi-config poiId="soccer" />
      <kpn-poi-config poiId="sportscentre" />
      <kpn-poi-config poiId="surfing" />
      <kpn-poi-config poiId="swimming" />
      <kpn-poi-config poiId="tennis" />
      <kpn-poi-config poiId="volleyball" />
    </kpn-poi-group>
  `,
  standalone: true,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupSportsComponent {}
