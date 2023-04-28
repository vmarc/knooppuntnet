import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-legend-line',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <svg height="3" width="25">
      <line x1="0" y1="1" x2="25" y2="1" [style]="style()" />
    </svg>
  `,
})
export class LegendLineComponent {
  @Input() color: string;

  style(): string {
    return `stroke:${this.color};stroke-width:3`;
  }
}
