import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'kpn-route-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control">
      <button
        class="button"
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
        position: absolute;
        left: 8px;
        top: 112px;
        z-index: 100;
        height: 31px;
        width: 31px;
        padding: 2px;
        border: 0 none white;
        border-radius: 3px;
        background-color: rgba(255, 255, 255, 0.5);
      }

      .button {
        margin: 1px;
        background-color: rgba(0, 60, 136, 0.5);
        color: white;
        border: 0 none white;
        border-radius: 2px;
        text-align: center;
        line-height: 28.5px;
        height: 28.5px;
        width: 28.5px;
      }

      .button:hover {
        background-color: rgba(0, 60, 136, 0.7);
      }
    `,
  ],
})
export class RouteControlComponent {
  @Output() action = new EventEmitter<void>();
}
