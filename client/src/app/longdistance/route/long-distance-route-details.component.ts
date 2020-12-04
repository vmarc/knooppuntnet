import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import {mergeMap} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {LongDistanceRoute} from '../../kpn/api/common/longdistance/long-distance-route';
import {ApiResponse} from '../../kpn/api/custom/api-response';

@Component({
  selector: 'kpn-long-distance-route-details',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-long-distance-route-page-header pageName="details"></kpn-long-distance-route-page-header>

    <div *ngIf="response$ | async as response" class="kpn-spacer-above">

      <div *ngIf="!response.result">
        Route not found
      </div>

      <div *ngIf="response.result as route">

        <kpn-data title="Summary">
          <p *ngIf="route.ref">{{route.ref}}</p>
          <p>{{route.name}}</p>
          <p class="kpn-separated">
            <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
            <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          </p>
          <p *ngIf="route.website">
            <a href="{{route.website}}" target="_blank" rel="nofollow noreferrer" class="external">website</a>
          </p>
        </kpn-data>

        <kpn-data title="Operator">
          {{route.operator}}
        </kpn-data>

        <kpn-data title="OSM">
          <p>
            {{route.wayCount}} ways
          </p>
          <p>
            {{route.osmDistance}}km
          </p>
        </kpn-data>

        <kpn-data title="GPX">
          <p>
            {{route.gpxFilename}}
          </p>
          <p>
            {{route.gpxDistance}}km
          </p>
        </kpn-data>

        <button mat-raised-button color="primary" (click)="gpxUpload()">GPX Upload</button>
      </div>
    </div>
  `,
  styles: [`
  `]
})
export class LongDistanceRouteDetailsComponent implements OnInit {

  response$: Observable<ApiResponse<LongDistanceRoute>>;

  constructor(private activatedRoute: ActivatedRoute,
              private appService: AppService) {
  }

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      map(params => {
        return params['routeId'];
      }),
      mergeMap(routeId => this.appService.longDistanceRoute(routeId))
    );
  }

  gpxUpload(): void {

  }


}
