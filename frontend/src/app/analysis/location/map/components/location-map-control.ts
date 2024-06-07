import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';

@Component({
  selector: 'kpn-location-map-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="ol-control map-control location-map-control">
      <button
        (click)="action.emit()"
        title="zoom to fit entire location"
        i18n-title="@@location-map-control.title"
        i18n="@@location-map-control.letter"
      >
        L
      </button>
    </div>
  `,
  styles: `
    .location-map-control {
      left: 8px;
      top: 112px;
    }
  `,
  standalone: true,
})
export class LocationMapControlComponent {
  action = output<void>();
}
