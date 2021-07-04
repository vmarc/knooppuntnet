import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

/* tslint:disable:template-i18n */
@Component({
  selector: 'kpn-legend-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
export class LegendIconComponent {
  @Input() color: string;
  @Input() proposed: boolean;

  style(): string {
    const standard = `stroke:${this.color};stroke-width:3;`;
    if (this.proposed) {
      return standard + 'stroke-dasharray:5;';
    }
    return standard;
  }
}
