import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: "kpn-indicator-dialog",
  template: `
    <div mat-dialog-title>
      <kpn-indicator-icon [letter]="letter" [color]="color"></kpn-indicator-icon>
      <ng-content select="[dialog-title]"></ng-content>
    </div>
    <div mat-dialog-content>
      <ng-content select="[dialog-body]"></ng-content>
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="onClick()">Close</button>
    </div>
  `
})
export class IndicatorDialogComponent {

  @Input() letter: string;
  @Input() color: string;

  @Output() closeDialog = new EventEmitter<void>();

  onClick() {
    this.closeDialog.emit();
  }

}
