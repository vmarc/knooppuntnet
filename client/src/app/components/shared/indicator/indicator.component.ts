import { ChangeDetectionStrategy } from '@angular/core';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'kpn-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="indicator" (click)="onOpenDialog()">
      <kpn-indicator-icon
        [letter]="letter"
        [color]="color"
      ></kpn-indicator-icon>
    </div>
  `,
  styles: [
    `
      .indicator {
        display: inline-block;
        padding-left: 5px;
        padding-right: 5px;
      }
    `,
  ],
})
export class IndicatorComponent {
  @Input() letter: string;
  @Input() color: string;

  @Output() openDialog = new EventEmitter<void>();

  onOpenDialog() {
    this.openDialog.emit();
  }
}
