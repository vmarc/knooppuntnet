import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-network-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control ol-control">
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
  styles: [
    `
      .control {
        left: 8px;
        top: 110px;
        z-index: 100;
        padding: 2px;
        width: 28px;
        height: 28px;
      }
    `,
  ],
})
export class NetworkControlComponent {
  @Output() action = new EventEmitter<void>();
}
