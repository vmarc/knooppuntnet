import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Observable } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <mat-spinner *ngIf="showSpinner$ | async" diameter="40" /> `,
  standalone: true,
  imports: [NgIf, MatProgressSpinnerModule, AsyncPipe],
})
export class SpinnerComponent implements OnInit {
  showSpinner$: Observable<boolean>;

  constructor(private spinnerService: SpinnerService) {}

  ngOnInit(): void {
    this.showSpinner$ = this.spinnerService.spinnerState$.pipe(
      debounceTime(300)
    );
  }
}
