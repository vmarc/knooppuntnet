import { Input } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox/checkbox';
import { LocationEditPage } from '@api/common/location/location-edit-page';
import { Store } from '@ngrx/store';
import { List, Range } from 'immutable';
import { Subscription } from 'rxjs';
import { TimeoutError } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { concat } from 'rxjs';
import { Observable } from 'rxjs';
import { delay } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { actionSharedHttpError } from '../../../core/shared/shared.actions';

@Component({
  selector: 'kpn-location-edit',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p>
      <mat-checkbox
        [checked]="nodeSelection"
        (change)="nodeSelectionChanged($event)"
      >
        {{ page.summary.nodeCount }}
        <span i18n="@@location-edit.nodes">nodes (quick)</span>
      </mat-checkbox>
    </p>
    <p>
      <mat-checkbox
        [checked]="routeRelationsSelection"
        (change)="routeRelationsSelectionChanged($event)"
      >
        {{ page.summary.routeCount }}
        <span i18n="@@location-edit.routes">routes relations (quick)</span>
      </mat-checkbox>
    </p>
    <p>
      <mat-checkbox
        [checked]="fullRouteSelection"
        (change)="fullRouteSelectionChanged($event)"
      >
        {{ page.summary.routeCount }}
        <span i18n="@@location-edit.full-routes"
          >routes with ways (takes more time)</span
        >
      </mat-checkbox>
    </p>
    <p *ngIf="showEstimatedTime$ | async">
      <i i18n="@@location-edit.time-warning">
        We estimate that it will take perhaps about {{ seconds }} seconds to
        load all nodes and routes in the editor.
      </i>
    </p>
    <p *ngIf="showProgress$ | async">
      <mat-progress-bar [value]="progress$ | async"></mat-progress-bar>
    </p>
    <p *ngIf="ready$ | async" i18n="@@location-edit.ready">Ready</p>
    <p *ngIf="error$ | async" i18n="@@location-edit.error">Error</p>
    <p *ngIf="errorName$ | async as errorName">
      {{ errorName }}
    </p>
    <p *ngIf="errorMessage$ | async as errorMessage">
      {{ errorMessage }}
    </p>
    <p *ngIf="timeout$ | async" class="timeout" i18n="@@location-edit.timeout">
      Timeout: editor not started, or editor remote control not enabled?
    </p>
    <p
      *ngIf="showProgress$ | async; else showEdit"
      i18n="@@location-edit.cancel"
    >
      <button mat-raised-button (click)="cancel()">Cancel</button>
    </p>
    <ng-template #showEdit>
      <p>
        <button
          mat-raised-button
          color="primary"
          (click)="edit()"
          title="Open in editor (like JOSM)"
          i18n-title="@@location-edit.submit.tooltip"
          i18n="@@location-edit.submit"
        >
          Load in JOSM editor
        </button>
      </p>
    </ng-template>
  `,
  styles: [
    `
      mat-progress-bar {
        width: 80%;
      }

      .timeout {
        color: red;
      }
    `,
  ],
})
export class LocationEditComponent implements OnInit {
  @Input() page: LocationEditPage;

  progressCount = 0;
  progressSteps = 0;
  seconds = 0;

  nodeSelection = true;
  routeRelationsSelection = true;
  fullRouteSelection = false;

  subscription: Subscription;

  progress$ = new BehaviorSubject<number>(0);
  showProgress$ = new BehaviorSubject<boolean>(false);
  ready$ = new BehaviorSubject<boolean>(false);
  error$ = new BehaviorSubject<boolean>(false);
  errorName$ = new BehaviorSubject<string>('');
  errorMessage$ = new BehaviorSubject<string>('');
  timeout$ = new BehaviorSubject<boolean>(false);
  showEstimatedTime$ = new BehaviorSubject<boolean>(false);

  private readonly nodeChunkSize = 50;
  private readonly routeChunkSize = 50;
  private readonly requestDelay = 200;
  private readonly josmUrl = 'http://localhost:8111/';
  private readonly apiUrl =
    this.josmUrl + 'import?url=https://api.openstreetmap.org/api/0.6';

  constructor(private appService: AppService, private store: Store<AppState>) {}

  ngOnInit(): void {
    this.updateExpectation();
  }

  nodeSelectionChanged(event: MatCheckboxChange) {
    this.nodeSelection = event.checked;
    this.updateExpectation();
  }

  routeRelationsSelectionChanged(event: MatCheckboxChange) {
    this.routeRelationsSelection = event.checked;
    this.updateExpectation();
  }

  fullRouteSelectionChanged(event: MatCheckboxChange) {
    this.fullRouteSelection = event.checked;
    this.updateExpectation();
  }

  edit(): void {
    this.store.dispatch(actionSharedHttpError({ httpError: null }));

    this.error$.next(false);
    this.timeout$.next(false);
    this.ready$.next(false);

    const nodeEdits = this.buildNodeEdits();
    const routeEdits = this.buildRouteEdits();
    const fullRouteEdits = this.buildFullRouteEdits();
    const edits = nodeEdits.concat(routeEdits).concat(fullRouteEdits);
    const setBounds = this.buildSetBounds();
    const steps = setBounds === null ? edits : edits.push(setBounds);

    this.progressSteps = steps.size;
    this.showProgress$.next(true);

    this.subscription = concat(...steps.toArray()).subscribe(
      (result) => {},
      (err) => {
        if (err instanceof TimeoutError) {
          this.timeout$.next(true);
          this.showProgress$.next(false);
        } else {
          this.showProgress$.next(false);
          this.error$.next(true);
          this.errorName$.next(err.name);
          this.errorMessage$.next(err.message);
        }
      },
      () => {
        this.showProgress$.next(false);
        this.progress$.next(0);
        this.progressCount = 0;
        this.progressSteps = 0;
        this.ready$.next(true);
        this.subscription.unsubscribe();
      }
    );
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

  buildSetBounds(): Observable<Object> {
    if (!this.page.nodeIds.isEmpty()) {
      const zoomUrl =
        this.josmUrl +
        `zoom?left=${this.page.bounds.minLon}&right=${this.page.bounds.maxLon}&top=${this.page.bounds.maxLat}&bottom=${this.page.bounds.minLat}`;
      return this.appService
        .edit(zoomUrl)
        .pipe(tap(() => this.updateProgress()));
    }
    return null;
  }

  private updateExpectation(): void {
    let nodeStepCount = 0;
    if (this.nodeSelection === true) {
      nodeStepCount += this.page.nodeIds.size / this.nodeChunkSize + 1;
    }

    let routeStepCount = 0;
    if (this.routeRelationsSelection === true) {
      routeStepCount += this.page.routeIds.size / this.routeChunkSize + 1;
    }

    let fullRouteStepCount = 0;
    if (this.fullRouteSelection === true) {
      fullRouteStepCount += this.page.routeIds.size;
    }

    const stepCount = nodeStepCount + routeStepCount + fullRouteStepCount;
    this.seconds = Math.round((stepCount * (this.requestDelay + 200)) / 1000);

    this.showEstimatedTime$.next(this.seconds > 3);
    this.timeout$.next(false);
  }

  private buildNodeEdits(): List<Observable<Object>> {
    if (!this.nodeSelection) {
      return List();
    }
    const nodeBatches = Range(0, this.page.nodeIds.count(), this.nodeChunkSize)
      .map((chunkStart) =>
        this.page.nodeIds.slice(chunkStart, chunkStart + this.nodeChunkSize)
      )
      .toList();
    return nodeBatches.map((nodeIds) => {
      const nodeIdString = nodeIds.join(',');
      const url = `${this.apiUrl}/nodes?nodes=${nodeIdString}`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.requestDelay)
      );
    });
  }

  private buildRouteEdits(): List<Observable<Object>> {
    if (!this.routeRelationsSelection || this.fullRouteSelection) {
      return List();
    }
    const routeBatches = Range(
      0,
      this.page.routeIds.count(),
      this.routeChunkSize
    )
      .map((chunkStart) =>
        this.page.routeIds.slice(chunkStart, chunkStart + this.routeChunkSize)
      )
      .toList();
    return routeBatches.map((routeIds) => {
      const routeIdString = routeIds.join(',');
      const url = `${this.apiUrl}/relations?relations=${routeIdString}`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.requestDelay)
      );
    });
  }

  private buildFullRouteEdits(): List<Observable<Object>> {
    if (!this.fullRouteSelection) {
      return List();
    }
    return this.page.routeIds.map((routeId) => {
      const url = `${this.apiUrl}/relation/${routeId}/full`;
      return this.appService.edit(url).pipe(
        tap(() => this.updateProgress()),
        delay(this.requestDelay)
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
