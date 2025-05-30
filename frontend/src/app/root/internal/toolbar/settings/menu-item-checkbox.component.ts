import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';

@Component({
  selector: 'ui-menu-item-checkbox',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <label
      nz-checkbox
      [nzChecked]="value()"
      (nzCheckedChange)="toggleValue()"
      [nzDisabled]="disabled()"
    >
      {{ label() }}
    </label>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [FormsModule, NzCheckboxComponent],
})
export class MenuItemCheckboxComponent {
  readonly label = input.required<string>();
  readonly value = input.required<boolean>();
  readonly disabled = input<boolean>(false);
  readonly toggle = output<void>();

  toggleValue(): void {
    this.toggle.emit();
  }
}
