import { ChangeDetectionStrategy, Component } from '@angular/core';
import { NzDividerComponent } from 'ng-zorro-antd/divider';

@Component({
  selector: 'ui-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <b>
      <ng-content select="[dialog-title]" />
    </b>
    <nz-divider nzSize="small" />
    <ng-content />
  `,
  imports: [NzDividerComponent],
})
export class DialogComponent {}
