import {ChangeDetectionStrategy} from '@angular/core';
import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {PageWidthService} from '../../components/shared/page-width.service';
import {LongDistanceRoute} from '../../kpn/api/common/longdistance/long-distance-route';
import {ApiResponse} from '../../kpn/api/custom/api-response';

@Component({
  selector: 'kpn-long-distance-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

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
  `,
  styles: [`
  `]
})
export class LongDistanceRoutesTableComponent implements OnInit {

  response$: Observable<ApiResponse<LongDistanceRoute[]>>;

  dataSource: MatTableDataSource<LongDistanceRoute>;
  displayedColumns$: Observable<Array<string>>;

  constructor(private pageWidthService: PageWidthService,
              private appService: AppService) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(map(() => this.displayedColumns()));
  }

  ngOnInit(): void {
    this.response$ = this.appService.longDistanceRoutes();
    this.response$.subscribe(response => {
      if (response.result) {
        this.dataSource.data = response.result;
      }
    });
  }

  displayedColumns(): string[] {
    return ['id', 'ref', 'gpx', 'name', 'operator', 'distance', 'wayCount', 'website', 'relation'];
  }
}
