import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'ui-form-error',
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <div class="ant-form-item-explain">
      <div class="ant-form-item-explain-error">
        {{ error() }}
      </div>
    </div>
  `,
})
export class FormErrorComponent {
  readonly error = input.required<string>();
}
