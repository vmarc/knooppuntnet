import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { SpinnerService } from './spinner.service';

@Component({
  selector: 'kpn-spinner',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-spinner *ngIf="showSpinner$ | async" diameter="40"/>
  `,
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
