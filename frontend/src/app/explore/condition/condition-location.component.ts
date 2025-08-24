import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NzInputDirective } from 'ng-zorro-antd/input';
import { ConditionLocationForm } from './condition-controls';

@Component({
  selector: 'ui-condition-location',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div>name</div>
      <div>
        <input nz-input [formControl]="form().controls.name" />
      </div>
    </div>
  `,
  imports: [FormsModule, NzInputDirective, ReactiveFormsModule],
})
export class ConditionLocationComponent {
  readonly form = input.required<ConditionLocationForm>();
}
