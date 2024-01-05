import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { debounceTime } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (showSpinner$ | async) {
      <mat-spinner diameter="40" />
    }
  `,
  standalone: true,
  imports: [MatProgressSpinnerModule, AsyncPipe],
})
export class SpinnerComponent {
  private readonly spinnerService = inject(SpinnerService);
  protected readonly showSpinner$ = this.spinnerService.spinnerState$.pipe(debounceTime(300));
}
