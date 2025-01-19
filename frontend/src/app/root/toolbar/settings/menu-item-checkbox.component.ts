import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TuiLabel } from '@taiga-ui/core';
import { TuiOption } from '@taiga-ui/core';
import { TuiCheckbox } from '@taiga-ui/kit';

@Component({
  selector: 'kpn-menu-item-checkbox',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <label tuiOption tuiLabel class="checkbox-line">
      <input
        tuiCheckbox
        type="checkbox"
        size="s"
        [ngModel]="value()"
        (click)="toggleValue()"
        [disabled]="disabled()"
      />
      <span>
        {{ label() }}
      </span>
    </label>
  `,
  imports: [TuiCheckbox, FormsModule, TuiOption, TuiLabel],
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
