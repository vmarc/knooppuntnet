import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FactLevel } from '@api/common/fact-level';

@Component({
  selector: 'ui-fact-level',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @switch (factLevel()) {
      @case ('info') {
        <div class="info circle"></div>
      }
      @case ('error') {
        <div class="error circle"></div>
      }
      @case ('other') {
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
})
export class FactLevelComponent {
  readonly factLevel = input.required<FactLevel>();
}
