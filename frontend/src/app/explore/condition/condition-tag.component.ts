import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { NzAutocompleteComponent } from 'ng-zorro-antd/auto-complete';
import { NzAutocompleteTriggerDirective } from 'ng-zorro-antd/auto-complete';
import { NzInputDirective } from 'ng-zorro-antd/input';
import { NzSelectComponent } from 'ng-zorro-antd/select';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { ConditionTagForm } from './condition-controls';

@Component({
  selector: 'ui-condition-tag',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="condition-line">
      <div>
        <div>tag key</div>
        <div>
          <input nz-input [formControl]="form().controls.key" [nzAutocomplete]="auto" />
          <nz-autocomplete [nzDataSource]="options" nzBackfill #auto />
        </div>
      </div>

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
        <div>tag value</div>
        <div>
          <input nz-input [formControl]="form().controls.value" />
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

    form {
      display: flex;
      align-items: center;
      gap: 0.5em;
    }

    .operator {
      width: 8em;
    }
  `,
  imports: [
    FormsModule,
    NzAutocompleteComponent,
    NzAutocompleteTriggerDirective,
    NzInputDirective,
    NzOptionComponent,
    NzSelectComponent,
    ReactiveFormsModule,
  ],
})
export class ConditionTagComponent {
  readonly form = input.required<ConditionTagForm>();

  readonly options = ['operator', 'symbol', 'option3', 'option4'];
}
