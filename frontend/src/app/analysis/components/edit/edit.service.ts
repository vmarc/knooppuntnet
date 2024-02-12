import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { actionSharedHttpError } from '@app/core';
import { ApiService } from '@app/services';
import { Store } from '@ngrx/store';
import { Range } from 'immutable';
import { Subscription } from 'rxjs';
import { TimeoutError } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { concat } from 'rxjs';
import { Observable } from 'rxjs';
import { delay } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { EditConfiguration } from './edit-configuration';
import { EditParameters } from './edit-parameters';

@Injectable()
export class EditService {
  private readonly apiService = inject(ApiService);
  private readonly store = inject(Store);

  private progressCount = 0;
  private progressSteps = 0;

  private subscription: Subscription;

  public readonly progress$ = new BehaviorSubject<number>(0);
  public readonly showProgress$ = new BehaviorSubject<boolean>(false);
  public readonly ready$ = new BehaviorSubject<boolean>(false);
  public readonly error$ = new BehaviorSubject<boolean>(false);
  public readonly errorName$ = new BehaviorSubject<string>('');
  public readonly errorMessage$ = new BehaviorSubject<string>('');
  public readonly timeout$ = new BehaviorSubject<boolean>(false);
  public readonly errorCouldNotConnect$ = new BehaviorSubject<boolean>(false);

  private readonly configuration = new EditConfiguration();

  edit(parameters: EditParameters): void {
    this.store.dispatch(actionSharedHttpError({ httpError: null }));

    const nodeEdits = this.buildNodeEdits(parameters);
    const wayEdits = this.buildWayEdits(parameters);
    const relationEdits = this.buildRelationEdits(parameters);
    const fullRelationEdits = this.buildFullRelationEdits(parameters);
    const edits = nodeEdits.concat(wayEdits).concat(relationEdits).concat(fullRelationEdits);
    const setBounds = this.buildSetBounds(parameters);
    const steps = setBounds === null ? edits : edits.concat(setBounds);

    this.progressSteps = steps.length;
    this.showProgress$.next(true);
    this.subscription = concat(...steps).subscribe({
      error: (err) => {
        if (err instanceof TimeoutError) {
          this.timeout$.next(true);
          this.showProgress$.next(false);
        } else if (err instanceof HttpErrorResponse) {
          const httpErrorResponse = err as HttpErrorResponse;
          this.showProgress$.next(false);
          this.error$.next(true);
          if (httpErrorResponse.status === 0) {
            this.errorCouldNotConnect$.next(true);
          } else {
            this.errorName$.next(httpErrorResponse.name);
            this.errorMessage$.next(httpErrorResponse.message);
          }
        } else {
          this.errorName$.next(err.name);
          this.errorMessage$.next(err.message);
        }
      },
      complete: () => {
        this.showProgress$.next(false);
        this.progress$.next(0);
        this.progressCount = 0;
        this.progressSteps = 0;
        this.ready$.next(true);
        if (this.subscription) {
          this.subscription.unsubscribe();
        }
      },
    });
  }

  cancel(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
      this.showProgress$.next(false);
      this.progress$.next(0);
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
    this.progress$.next(progress);
  }
}
