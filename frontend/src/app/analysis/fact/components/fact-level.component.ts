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
        <div class="color-info circle"></div>
      }
      @case ('error') {
        <div class="color-error circle"></div>
      }
      @case ('other') {
        <div class="color-other circle"></div>
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
  `,
})
export class FactLevelComponent {
  readonly factLevel = input.required<FactLevel>();
}
