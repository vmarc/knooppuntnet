import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-route-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="ol-control geolocation-control">
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
      .geolocation-control {
        left: 8px;
        top: 110px;
        z-index: 100;
      }
    `,
  ],
  standalone: true,
})
export class RouteControlComponent {
  @Output() action = new EventEmitter<void>();
}
