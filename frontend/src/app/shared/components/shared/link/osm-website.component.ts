import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-osm-website',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <a class="external" rel="nofollow noreferrer" href="https://www.openstreetmap.org">
      OpenStreetMap
    </a>
  `,
  standalone: true,
})
export class OsmWebsiteComponent {}
