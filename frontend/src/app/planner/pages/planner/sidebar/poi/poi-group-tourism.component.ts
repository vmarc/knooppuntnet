import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PoiConfigComponent } from './poi-config.component';
import { PoiGroupComponent } from './poi-group.component';

@Component({
  selector: 'ui-poi-group-tourism',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-poi-group name="tourism" title="Tourism" i18n-title="@@poi.group.tourism">
      <ui-poi-config poiId="arts-centre" />
      <ui-poi-config poiId="artwork" />
      <ui-poi-config poiId="casino" />
      <ui-poi-config poiId="gallery" />
      <ui-poi-config poiId="monumental-tree" />
      <ui-poi-config poiId="museum" />
      <ui-poi-config poiId="vineyard" />
      <ui-poi-config poiId="tourism" />
    </ui-poi-group>
  `,
  imports: [PoiGroupComponent, PoiConfigComponent],
})
export class PoiGroupTourismComponent {}
