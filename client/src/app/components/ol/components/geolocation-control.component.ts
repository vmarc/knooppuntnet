import {EventEmitter} from "@angular/core";
import {Output} from "@angular/core";
import {ChangeDetectionStrategy, Component} from "@angular/core";

@Component({
  selector: "kpn-geolocation-control",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="control">
      <button
        class="button"
        (click)="action.emit()"
        title="position the map on your current location"
        i18n-title="@@geolocation-control.title">
        <mat-icon svgIcon="location"></mat-icon>
      </button>
    </div>
  `,
  styles: [`
    .control {
      position: absolute;
      left: 8px;
      top: 146px;
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
      height: 28.5px;
      width: 28.5px;
      padding-left: 3px;
      padding-top: 5px;
    }

    ::ng-deep mat-icon svg {
      width: 18px;
      height: 18px;
    }

    .button:hover {
      background-color: rgba(0, 60, 136, 0.7);
    }
  `]
})
export class GeolocationControlComponent {
  @Output() action = new EventEmitter<void>();
}
