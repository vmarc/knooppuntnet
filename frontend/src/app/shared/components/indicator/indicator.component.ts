import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { IndicatorIconComponent } from './indicator-icon.component';

@Component({
  selector: 'ui-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="indicator" (click)="onOpenDialog()">
      <ui-indicator-icon [letter]="letter()" [color]="color()" />
    </div>
  `,
  styles: `
    .indicator {
      display: inline-block;
      padding-left: 5px;
      padding-right: 5px;
    }
  `,
  imports: [IndicatorIconComponent],
})
export class IndicatorComponent {
  readonly letter = input.required<string>();
  readonly color = input.required<string>();

  readonly openDialog = output<void>();

  onOpenDialog() {
    this.openDialog.emit();
  }
}
