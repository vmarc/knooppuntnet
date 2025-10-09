import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzDividerComponent } from 'ng-zorro-antd/divider';

@Component({
  selector: 'ui-divider',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      <nz-divider />
    </div>
  `,
  imports: [NzDividerComponent],
})
export class DividerComponent {}
