import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Store } from '@ngrx/store';
import { AppState } from '../../../../core/core.state';
import { actionLongdistanceRouteDetailsInit } from '../../../store/monitor.actions';
import { selectLongdistanceRouteDetailsPage } from '../../../store/monitor.selectors';
import { selectLongdistanceRouteId } from '../../../store/monitor.selectors';

@Component({
  selector: 'kpn-longdistance-route-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-longdistance-route-page-header
      pageName="details"
      [routeId]="routeId$ | async"
    ></kpn-longdistance-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">
      <div *ngIf="!response.result">Route not found</div>

      <div *ngIf="response.result as route">
        <kpn-data title="Summary">
          <p *ngIf="route.ref">{{ route.ref }}</p>
          <p>{{ route.name }}</p>
          <p class="kpn-separated">
            <kpn-osm-link-relation
              [relationId]="route.id"
            ></kpn-osm-link-relation>
            <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          </p>
          <p *ngIf="route.website">
            <a
              href="{{ route.website }}"
              target="_blank"
              rel="nofollow noreferrer"
              class="external"
              >website</a
            >
          </p>
        </kpn-data>

        <kpn-data title="Operator">
          {{ route.operator }}
        </kpn-data>

        <kpn-data title="OSM">
          <p>{{ route.wayCount }} ways</p>
          <p>{{ route.osmDistance }}km</p>
        </kpn-data>

        <kpn-data title="GPX">
          <p>
            {{ route.gpxFilename }}
          </p>
          <p>{{ route.gpxDistance }}km</p>
        </kpn-data>

        <kpn-data title="Analysis">
          <p *ngIf="route.happy">
            <span>All ok</span>
          </p>
          <div
            *ngIf="
              !route.gpxFilename &&
              route.osmSegmentCount == 1 &&
              route.gpxNokSegmentCount == 0
            "
          >
            <p>No GPX, so no known deviations.</p>
            <p>The OSM route looks ok: a GPX trace can be created from it.</p>
          </div>
          <div *ngIf="route.osmSegmentCount > 1" class="kpn-line warning-line">
            <div>
              <mat-icon svgIcon="warning" class="warning-icon"></mat-icon>
            </div>
            <span
              >The OSM route relation contains
              {{ route.osmSegmentCount }} segments. It will not be possible to
              create a GPX trace from it.</span
            >
          </div>
          <div
            *ngIf="route.gpxNokSegmentCount > 0"
            class="kpn-line warning-line"
          >
            <div>
              <mat-icon svgIcon="warning" class="warning-icon"></mat-icon>
            </div>
            <span
              >There are {{ route.gpxNokSegmentCount }} segments in the GPX
              trace where the distance to the closest OSM way is more than 10
              meters.</span
            >
          </div>
        </kpn-data>

        <div class="kpn-button-group">
          <button mat-raised-button color="primary" (click)="gpxDownload()">
            Download GPX file
          </button>
          <button mat-raised-button (click)="gpxUpload()">
            Upload GPX file
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .warning-line {
        padding-bottom: 1em;
      }

      .warning-icon {
        width: 2em;
        height: 2em;
      }
    `,
  ],
})
export class LongdistanceRouteDetailsComponent implements OnInit {
  readonly routeId$ = this.store.select(selectLongdistanceRouteId);
  readonly response$ = this.store.select(selectLongdistanceRouteDetailsPage);

  constructor(private snackBar: MatSnackBar, private store: Store<AppState>) {}

  ngOnInit(): void {
    this.store.dispatch(actionLongdistanceRouteDetailsInit());
  }

  gpxUpload(): void {
    this.snackBar.open('Sorry, GPX file upload not implemented yet', 'close', {
      panelClass: ['mat-toolbar', 'mat-primary'],
    });
  }

  gpxDownload(): void {
    this.snackBar.open(
      'Sorry, GPX file download not implemented yet',
      'close',
      { panelClass: ['mat-toolbar', 'mat-primary'] }
    );
  }
}
