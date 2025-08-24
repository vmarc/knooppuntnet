import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NzInputDirective } from 'ng-zorro-antd/input';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { ConditionRouteNameForm } from './condition-controls';

@Component({
  selector: 'ui-condition-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="condition-line">
      <div>
        <div>operation</div>
        <div>
          <nz-select [formControl]="form().controls.operator">
            <nz-option nzValue="equals" nzLabel="equals" />
            <nz-option nzValue="contains" nzLabel="contains" />
          </nz-select>
        </div>
      </div>

      <div>
        <div>route name</div>
        <div>
          <input nz-input [formControl]="form().controls.name" />
        </div>
      </div>
    </div>
  `,
  styles: `
    .condition-line {
      display: flex;
      padding-left: 0.5em;
      gap: 0.5em;
    }

    .operator {
      width: 8em;
    }

    form {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }
  `,
  imports: [
    FormsModule,
    NzInputDirective,
    NzOptionComponent,
    NzSelectComponent,
    ReactiveFormsModule,
  ],
})
export class ConditionNameComponent {
  readonly form = input.required<ConditionRouteNameForm>();
}
