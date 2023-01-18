import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'kpn-osm-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a
      class="external"
      rel="nofollow noreferrer"
      target="_blank"
      href="https://www.openstreetmap.org/{{ kind }}/{{ elementId }}"
    >
      {{ title }}
    </a>
  `,
})
export class OsmLinkComponent {
  @Input() kind: string;
  @Input() elementId: string;
  @Input() title: string;
}
