import { HttpErrorResponse } from '@angular/common/http';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { EditStepBuilder } from '@app/analysis/components/edit/edit-step-builder';
import { Subscription } from 'rxjs';
import { TimeoutError } from 'rxjs';
import { concat } from 'rxjs';
import { SharedStateService } from '@app/shared/core/shared/shared-state.service';
import { EditParameters } from './edit-parameters';

@Injectable()
export class EditService {
  private readonly editStepBuilder = inject(EditStepBuilder);
  private readonly sharedStateService = inject(SharedStateService);

  private readonly _progress = signal<number>(0);
  private readonly _showProgress = signal<boolean>(false);
  private readonly _ready = signal<boolean>(false);
  private readonly _error = signal<boolean>(false);
  private readonly _errorName = signal<string>('');
  private readonly _errorMessage = signal<string>('');
  private readonly _timeout = signal<boolean>(false);
  private readonly _errorCouldNotConnect = signal<boolean>(false);

  readonly progress = this._progress.asReadonly();
  readonly showProgress = this._showProgress.asReadonly();
  readonly ready = this._ready.asReadonly();
  readonly error = this._error.asReadonly();
  readonly errorName = this._errorName.asReadonly();
  readonly errorMessage = this._errorMessage.asReadonly();
  readonly timeout = this._timeout.asReadonly();
  readonly errorCouldNotConnect = this._errorCouldNotConnect.asReadonly();

  private progressCount = 0;
  private progressSteps = 0;

  private subscription: Subscription;

  edit(parameters: EditParameters): void {
    this.sharedStateService.setHttpError(null);
    const steps = this.editStepBuilder.build(parameters, () => this.updateProgress());
    this.progressSteps = steps.length;
    this._showProgress.set(true);
    this.subscription = concat(...steps).subscribe({
      error: (err) => {
        if (err instanceof TimeoutError) {
          this._timeout.set(true);
          this._showProgress.set(false);
        } else if (err instanceof HttpErrorResponse) {
          const httpErrorResponse = err as HttpErrorResponse;
          this._showProgress.set(false);
          this._error.set(true);
          if (httpErrorResponse.status === 0) {
            this._errorCouldNotConnect.set(true);
          } else {
            this._errorName.set(httpErrorResponse.name);
            this._errorMessage.set(httpErrorResponse.message);
          }
        } else {
          this._errorName.set(err.name);
          this._errorMessage.set(err.message);
        }
      },
      complete: () => {
        this._showProgress.set(false);
        this._progress.set(0);
        this.progressCount = 0;
        this.progressSteps = 0;
        this._ready.set(true);
        if (this.subscription) {
          this.subscription.unsubscribe();
        }
      },
    });
  }

  cancel(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
      this._showProgress.set(false);
      this._progress.set(0);
      this.progressCount = 0;
      this.progressSteps = 0;
    }
  }

  private updateProgress(): void {
    this.progressCount = this.progressCount + 1;
    let progress = 0;
    if (this.progressSteps > 0) {
      progress = Math.round((100 * this.progressCount) / this.progressSteps);
    }
    this._progress.set(progress);
  }
}
