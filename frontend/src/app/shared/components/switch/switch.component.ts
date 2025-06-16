import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzSwitchComponent } from 'ng-zorro-antd/switch';

@Component({
  selector: 'ui-switch',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="switch" (click)="onToggleImpact()">
      <nz-switch nzSize="small" [ngModel]="value()" />
      <span>{{ label() }}</span>
    </div>
  `,
  styles: `
    .switch {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap: 0.3em;
      margin-right: 2em;
      cursor: pointer;
    }

    .switch > nz-switch {
      padding-bottom: 2px;
    }
  `,
  imports: [FormsModule, NzSwitchComponent],
})
export class SwitchComponent {
  readonly label = input.required<string>();
  readonly value = input.required<boolean>();
  readonly valueChange = output<boolean>();

  onToggleImpact(): void {
    this.valueChange.emit(!this.value());
  }
}
