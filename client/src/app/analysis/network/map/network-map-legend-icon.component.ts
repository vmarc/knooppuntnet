import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-network-map-legend-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <svg height="30" width="70">
      <line x1="0" y1="15" x2="15" y2="15" [style]="style()" />
      <circle cx="30" cy="15" r="13" [style]="style()" fill="white" />
      <line x1="45" y1="15" x2="60" y2="15" [style]="style()" />
      <text
        x="30"
        y="15"
        text-anchor="middle"
        dominant-baseline="middle"
        font-style="normal"
        font-size="12px"
      >
        01
      </text>
    </svg>
  `,
})
export class NetworkMapLegendIconComponent {
  @Input() color: string;

  style(): string {
    return `stroke:${this.color};stroke-width:3`;
  }
}
