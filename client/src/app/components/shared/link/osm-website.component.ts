import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-osm-website',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      rel="nofollow noreferrer"
      href="https://www.openstreetmap.org"
    >
      OpenStreetMap
    </a>
  `,
})
export class OsmWebsiteComponent {}
