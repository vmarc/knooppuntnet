import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'ui-osm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      rel="nofollow noreferrer"
      target="_blank"
      [href]="'https://www.openstreetmap.org/' + kind() + '/' + elementId()"
      title="Open in OpenStreetMap website"
      i18n-title="@@osm-link.title"
    >
      {{ title() }}
    </a>
  `,
})
export class OsmLinkComponent {
  readonly kind = input.required<string>();
  readonly elementId = input.required<string>();
  readonly title = input.required<string>();
}
