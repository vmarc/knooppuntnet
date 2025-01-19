import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { TuiLoader } from '@taiga-ui/core';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.showSpinner()) {
      <tui-loader />
    }
  `,
  imports: [TuiLoader],
})
export class SpinnerComponent {
  readonly service = inject(SpinnerService);
}
