import {ChangeDetectionStrategy, Component} from "@angular/core";

@Component({
  selector: "kpn-timeout",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-icon-button class="close-button" [mat-dialog-close]="true">
      <mat-icon svgIcon="remove" i18n="@@timeout.close">close</mat-icon>
    </button>
    <p i18n="@@timeout.message-1">
      Sorry.
    </p>
    <p i18n="@@timeout.message-2">
      No response from editor within 5 seconds.
    </p>
    <p i18n="@@timeout.message-3">
      Has the editor (JOSM) been started? Has remote control been enabled within the browser?
    </p>
  `,
  styles: [`
    .close-button {
      background-color: white;
      float: right;
      top: -24px;
      right: -24px;
    }
    .mat-icon-button ::ng-deep .mat-button-focus-overlay {
      display: none;
    }
  `
  ]
})
export class TimeoutComponent {
}
