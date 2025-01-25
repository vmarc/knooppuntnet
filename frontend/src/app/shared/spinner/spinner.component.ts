import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzSpinComponent } from 'ng-zorro-antd/spin';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.showSpinner()) {
      <nz-spin nzSimple></nz-spin>
    }
  `,
  imports: [NzSpinComponent],
})
export class SpinnerComponent {
  readonly service = inject(SpinnerService);
}
