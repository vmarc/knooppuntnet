import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-network-control',
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
  styles: [
    `
      .network-control {
        left: 8px;
        top: 112px;
      }
    `,
  ],
  standalone: true,
})
export class NetworkControlComponent {
  @Output() action = new EventEmitter<void>();
}
