import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'ui-network-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="ol-control map-control network-control">
      <button
        (click)="action.emit()"
        title="zoom to fit entire network"
        i18n-title="@@network-control.title"
        i18n="@@network-control.letter"
      >
        N
      </button>
    </div>
  `,
  styles: `
    .network-control {
      left: 8px;
      top: 112px;
    }
  `,
})
export class NetworkControlComponent {
  readonly action = output<void>();
}
