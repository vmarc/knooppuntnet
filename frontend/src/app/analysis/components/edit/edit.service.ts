import { HttpErrorResponse } from '@angular/common/http';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ApiService } from '@app/shared/services/api.service';
import { Range } from 'immutable';
import { Subscription } from 'rxjs';
import { TimeoutError } from 'rxjs';
import { concat } from 'rxjs';
import { Observable } from 'rxjs';
import { delay } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { SharedStateService } from '../../../shared/core/shared/shared-state.service';
import { EditConfiguration } from './edit-configuration';
import { EditParameters } from './edit-parameters';

@Injectable()
export class EditService {
  private readonly apiService = inject(ApiService);
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

  private readonly configuration = new EditConfiguration();

  edit(parameters: EditParameters): void {
    this.sharedStateService.setHttpError(null);

    const nodeEdits = this.buildNodeEdits(parameters);
    const wayEdits = this.buildWayEdits(parameters);
    const relationEdits = this.buildRelationEdits(parameters);
    const fullRelationEdits = this.buildFullRelationEdits(parameters);
    const edits = nodeEdits.concat(wayEdits).concat(relationEdits).concat(fullRelationEdits);
    const setBounds = this.buildSetBounds(parameters);
    const steps = setBounds === null ? edits : edits.concat(setBounds);

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

  buildSetBounds(parameters: EditParameters): Observable<string> {
    if (parameters.bounds) {
      const zoomUrl =
        this.configuration.josmUrl +
        `zoom?left=${parameters.bounds.minLon}&right=${parameters.bounds.maxLon}&top=${parameters.bounds.maxLat}&bottom=${parameters.bounds.minLat}`;
      return this.apiService.edit(zoomUrl).pipe(tap(() => this.updateProgress()));
    }
    return null;
  }

  private buildNodeEdits(parameters: EditParameters): Observable<string>[] {
    if (!parameters.nodeIds || parameters.nodeIds.length === 0) {
      return [];
    }
    const nodeBatches = Range(0, parameters.nodeIds.length, this.configuration.nodeChunkSize)
      .map((chunkStart) =>
        parameters.nodeIds.slice(chunkStart, chunkStart + this.configuration.nodeChunkSize)
      )
      .toArray();
    return nodeBatches.map((nodeIds) => {
      const nodeIdString = nodeIds.join(',');
      const url = `${this.configuration.apiUrl}/nodes?nodes=${nodeIdString}`;
      return this.apiService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.configuration.requestDelay)
      );
    });
  }

  private buildWayEdits(parameters: EditParameters): Observable<string>[] {
    if (!parameters.wayIds || parameters.wayIds.length === 0) {
      return [];
    }

    return parameters.wayIds.map((wayId) => {
      const url = `${this.configuration.apiUrl}/way/${wayId}/full`;
      return this.apiService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.configuration.requestDelay)
      );
    });
  }

  private buildRelationEdits(parameters: EditParameters): Observable<string>[] {
    if (!parameters.relationIds || parameters.relationIds.length === 0) {
      return [];
    }
    const relationBatches = Range(
      0,
      parameters.relationIds.length,
      this.configuration.relationChunkSize
    )
      .map((chunkStart) =>
        parameters.relationIds.slice(chunkStart, chunkStart + this.configuration.relationChunkSize)
      )
      .toArray();
    return relationBatches.map((relationIds) => {
      const relationIdString = relationIds.join(',');
      const url = `${this.configuration.apiUrl}/relations?relations=${relationIdString}`;
      return this.apiService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.configuration.requestDelay)
      );
    });
  }

  private buildFullRelationEdits(parameters: EditParameters): Observable<string>[] {
    if (!parameters.fullRelation) {
      return [];
    }
    return parameters.relationIds.map((relationId) => {
      const url = `${this.configuration.apiUrl}/relation/${relationId}/full`;
      return this.apiService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.configuration.requestDelay)
      );
    });
  }

  private updateProgress() {
    this.progressCount = this.progressCount + 1;
    let progress = 0;
    if (this.progressSteps > 0) {
      progress = Math.round((100 * this.progressCount) / this.progressSteps);
    }
    this._progress.set(progress);
  }
}
