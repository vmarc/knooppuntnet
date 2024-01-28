import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'kpn-indicator-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="indicator-icon {{ color() }}">
      {{ letter() }}
    </div>
  `,
  styles: `
    .indicator-icon {
      color: white;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      border-radius: 50%;
      height: 25px;
      width: 25px;
      cursor: help;
    }

    .red {
      background-color: rgb(239, 83, 80);
    }

    .green {
      background-color: rgb(102, 187, 106);
    }

    .blue {
      background-color: rgb(33, 150, 243);
    }

    .orange {
      background-color: rgb(255, 163, 0);
    }

    .gray {
      background-color: rgb(224, 224, 224);
    }
  `,
  standalone: true,
})
export class IndicatorIconComponent {
  letter = input.required<string>();
  color = input.required<string>();
}
