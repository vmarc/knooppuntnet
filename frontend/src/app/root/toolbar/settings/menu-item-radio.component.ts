import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TuiLabel } from '@taiga-ui/core';
import { TuiOption } from '@taiga-ui/core';
import { TuiRadioComponent } from '@taiga-ui/kit';

@Component({
  selector: 'kpn-menu-item-radio',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <label tuiOption tuiLabel>
      <input
        tuiRadio
        type="radio"
        size="s"
        [name]="name()"
        [value]="value()"
        [ngModel]="actual()"
        (click)="toggleValue()"
      />
      <span>
        {{ label() }}
      </span>
    </label>
  `,
  imports: [FormsModule, TuiOption, TuiRadioComponent, TuiLabel],
})
export class MenuItemRadioComponent {
  readonly name = input.required<string>();
  readonly label = input.required<string>();
  readonly value = input.required<string>();
  readonly actual = input.required<string>();
  readonly changed = output<string>();

  toggleValue(): void {
    this.changed.emit(this.value());
  }
}
