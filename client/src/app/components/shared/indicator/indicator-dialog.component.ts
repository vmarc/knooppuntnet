import {ChangeDetectionStrategy} from "@angular/core";
import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: "kpn-indicator-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-dialog-title class="title">
      <kpn-indicator-icon [letter]="letter" [color]="color"></kpn-indicator-icon>
      <div class="title-text">
        <ng-content select="[dialog-title]"></ng-content>
      </div>
    </div>
    <div mat-dialog-content>
      <ng-content select="[dialog-body]"></ng-content>
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="onClick()">Close</button>
    </div>
  `,
  styles: [`

    .title {
      display: flex;
      align-items: center;
    }

    .title-text {
      display: inline-block;
      padding-top: 5px;
      padding-left: 20px;
    }
  `]
})
export class IndicatorDialogComponent {

  @Input() letter: string;
  @Input() color: string;

  @Output() closeDialog = new EventEmitter<void>();

  onClick() {
    this.closeDialog.emit();
  }

}
