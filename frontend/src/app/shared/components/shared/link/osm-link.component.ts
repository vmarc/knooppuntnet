import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-osm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      rel="nofollow noreferrer"
      target="_blank"
      href="https://www.openstreetmap.org/{{ kind() }}/{{ elementId() }}"
      title="Open in OpenStreetMap website"
      i18n-title="@@osm-link.title"
    >
      {{ title() }}
    </a>
  `,
  standalone: true,
})
export class OsmLinkComponent {
  kind = input.required<string>();
  elementId = input.required<string>();
  title = input.required<string>();
}
