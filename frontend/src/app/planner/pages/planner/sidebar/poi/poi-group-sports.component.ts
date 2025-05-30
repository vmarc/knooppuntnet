import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-sports',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="sports" title="Sports" i18n-title="@@poi.group.sports">
      <ui-poi-config poiId="american-football" />
      <ui-poi-config poiId="baseball" />
      <ui-poi-config poiId="basketball" />
      <ui-poi-config poiId="cycling" />
      <ui-poi-config poiId="gymnastics" />
      <ui-poi-config poiId="golf" />
      <ui-poi-config poiId="hockey" />
      <ui-poi-config poiId="horseracing" />
      <ui-poi-config poiId="icehockey" />
      <ui-poi-config poiId="soccer" />
      <ui-poi-config poiId="sportscentre" />
      <ui-poi-config poiId="surfing" />
      <ui-poi-config poiId="swimming" />
      <ui-poi-config poiId="tennis" />
      <ui-poi-config poiId="volleyball" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupSportsComponent {}
