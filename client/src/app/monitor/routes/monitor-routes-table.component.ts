import {OnInit} from '@angular/core';
import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {MonitorRouteDetail} from '@api/common/monitor/monitor-route-detail';
import {MonitorGroupPage} from '@api/common/monitor/monitor-group-page';
import {ApiResponse} from '@api/custom/api-response';
import {select} from '@ngrx/store';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {filter} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {PageWidthService} from '../../components/shared/page-width.service';
import {AppState} from '../../core/core.state';
import {actionMonitorRoutesInit} from '../store/monitor.actions';
import {selectMonitorRoutes} from '../store/monitor.selectors';

@Component({
  selector: 'kpn-monitor-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="response$ | async as response">

      <table mat-table [dataSource]="dataSource">

        <ng-container matColumnDef="id">
          <th mat-header-cell *matHeaderCellDef>Id</th>
          <td mat-cell *matCellDef="let route">
            <a [routerLink]="'/monitor/long-distance-routes/' + route.id">{{route.id}}</a>
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
export class MonitorRoutesTableComponent implements OnInit {

  readonly dataSource: MatTableDataSource<MonitorRouteDetail> = new MatTableDataSource();
  readonly displayedColumns$ = this.pageWidthService.current$.pipe(map(() => this.displayedColumns()));

  readonly response$: Observable<ApiResponse<MonitorGroupPage>> = this.store.pipe(
    select(selectMonitorRoutes),
    filter(r => r != null),
    tap(response => {
      if (response.result) {
        this.dataSource.data = response.result.details;
      }
    })
  );

  constructor(private pageWidthService: PageWidthService,
              private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.store.dispatch(actionMonitorRoutesInit());
  }

  displayedColumns(): string[] {
    return ['id', 'ref', 'gpx', 'gpxHappy', 'osmHappy', 'name', 'operator', 'distance', 'wayCount', 'website', 'relation'];
  }
}
