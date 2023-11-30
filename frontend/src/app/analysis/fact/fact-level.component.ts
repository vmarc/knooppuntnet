import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { FactLevel } from './fact-level';

@Component({
  selector: 'kpn-fact-level',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (factLevel) {
      @case (FactLevel.info) {
        <div class="info circle"></div>
      }
      @case (FactLevel.error) {
        <div class="error circle"></div>
      }
      @case (FactLevel.other) {
        <div class="other circle"></div>
      }
    }
  `,
  styles: `
    .circle {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
    }

    .info {
      background: rgb(102, 187, 106); /* material green400 */
    }

    .error {
      background: rgb(239, 83, 80); /* material red400 */
    }

    .other {
      background: rgb(255, 167, 38); /* material orange400 */
    }
  `,
  standalone: true,
})
export class FactLevelComponent {
  @Input() factLevel: FactLevel;

  protected readonly FactLevel = FactLevel;
}
