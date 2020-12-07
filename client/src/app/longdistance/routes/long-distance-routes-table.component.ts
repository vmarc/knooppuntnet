import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../components/shared/page-width.service';
import {AppState} from '../../core/core.state';
import {selectLongDistanceRoutes} from '../../core/longdistance/long-distance.selectors';
import {LongDistanceRouteDetail} from '../../kpn/api/common/longdistance/long-distance-route-detail';
import {LongDistanceRoutesPage} from '../../kpn/api/common/longdistance/long-distance-routes-page';
import {ApiResponse} from '../../kpn/api/custom/api-response';

@Component({
  selector: 'kpn-long-distance-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="response$ | async as response">

      <table mat-table [dataSource]="dataSource">

        <ng-container matColumnDef="id">
          <th mat-header-cell *matHeaderCellDef>Id</th>
          <td mat-cell *matCellDef="let route">
            <a [routerLink]="'/long-distance/routes/' + route.id">{{route.id}}</a>
          </td>
        </ng-container>

        <ng-container matColumnDef="ref">
          <th mat-header-cell *matHeaderCellDef>Ref</th>
          <td mat-cell *matCellDef="let route">
            {{route.ref}}
          </td>
        </ng-container>

        <ng-container matColumnDef="gpx">
          <th mat-header-cell *matHeaderCellDef>GPX</th>
          <td mat-cell *matCellDef="let route">
            <mat-icon *ngIf="route.gpxFilename" svgIcon="tick"></mat-icon>
          </td>
        </ng-container>

        <ng-container matColumnDef="gpxHappy">
          <th mat-header-cell *matHeaderCellDef>GPX</th>
          <td mat-cell *matCellDef="let route">
            <mat-icon *ngIf="route.gpxHappy" svgIcon="happy"></mat-icon>
          </td>
        </ng-container>

        <ng-container matColumnDef="osmHappy">
          <th mat-header-cell *matHeaderCellDef>OSM</th>
          <td mat-cell *matCellDef="let route">
            <mat-icon *ngIf="route.osmHappy" svgIcon="happy"></mat-icon>
          </td>
        </ng-container>

        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef>Name</th>
          <td mat-cell *matCellDef="let route">
            {{route.name}}
          </td>
        </ng-container>

        <ng-container matColumnDef="operator">
          <th mat-header-cell *matHeaderCellDef>Operator</th>
          <td mat-cell *matCellDef="let route">
            {{route.operator}}
          </td>
        </ng-container>

        <ng-container matColumnDef="distance">
          <th mat-header-cell *matHeaderCellDef>Distance</th>
          <td mat-cell *matCellDef="let route">
            {{route.osmDistance}}km
          </td>
        </ng-container>

        <ng-container matColumnDef="wayCount">
          <th mat-header-cell *matHeaderCellDef>Ways</th>
          <td mat-cell *matCellDef="let route">
            {{route.wayCount}}
          </td>
        </ng-container>

        <ng-container matColumnDef="website">
          <th mat-header-cell *matHeaderCellDef>Website</th>
          <td mat-cell *matCellDef="let route">
            <a *ngIf="route.website" href="{{route.website}}" target="_blank" rel="nofollow noreferrer" class="external">website</a>
            <span *ngIf="!route.website">-</span>
          </td>
        </ng-container>

        <ng-container matColumnDef="relation">
          <th mat-header-cell *matHeaderCellDef>Relation</th>
          <td mat-cell *matCellDef="let route" class="kpn-separated">
            <kpn-osm-link-relation [relationId]="route.id"></kpn-osm-link-relation>
            <kpn-josm-relation [relationId]="route.id"></kpn-josm-relation>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns()"></tr>
        <tr mat-row *matRowDef="let route; columns: displayedColumns();"></tr>
      </table>

    </div>
  `
})
export class LongDistanceRoutesTableComponent {

  dataSource: MatTableDataSource<LongDistanceRouteDetail> = new MatTableDataSource();
  displayedColumns$: Observable<Array<string>>;

  response$: Observable<ApiResponse<LongDistanceRoutesPage>> = this.store.pipe(
    select(selectLongDistanceRoutes),
    filter(r => r != null),
    tap(response => {
      if (response.result) {
        this.dataSource.data = response.result.details;
      }
    })
  );

  constructor(private pageWidthService: PageWidthService,
              private store: Store<AppState>) {
    this.displayedColumns$ = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
  }

  displayedColumns(): string[] {
    return ['id', 'ref', 'gpx', 'gpxHappy', 'osmHappy', 'name', 'operator', 'distance', 'wayCount', 'website', 'relation'];
  }
}
