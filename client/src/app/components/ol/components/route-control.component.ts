import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-route-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control ol-control">
      <button
        (click)="action.emit()"
        title="zoom to fit entire route"
        i18n-title="@@route-control.title"
        i18n="@@route-control.letter"
      >
        R
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
export class RouteControlComponent {
  @Output() action = new EventEmitter<void>();
}
