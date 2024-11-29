import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (service.showSpinner()) {
      <mat-spinner diameter="40" />
    }
  `,
  imports: [MatProgressSpinnerModule],
})
export class SpinnerComponent {
  protected readonly service = inject(SpinnerService);
}
