import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {RouteSummary} from '@api/common/route-summary';
import {TimeInfo} from '@api/common/time-info';
import {BehaviorSubject} from 'rxjs';
import {PaginatorComponent} from '../../../components/shared/paginator/paginator.component';
import {SubsetOrphanRouteFilter} from './subset-orphan-route-filter';
import {SubsetOrphanRouteFilterCriteria} from './subset-orphan-route-filter-criteria';
import {SubsetOrphanRoutesService} from './subset-orphan-routes.service';

@Component({
  selector: 'kpn-subset-orphan-routes-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      [length]="dataSource.data.length"
      [showPageSizeSelection]="true"
      [showFirstLastButtons]="true">
    </kpn-paginator>

    <table mat-table [dataSource]="dataSource" class="kpn-columns-table">

      <ng-container matColumnDef="rowNumber">
        <td mat-cell *matCellDef="let route; let i = index">
          {{rowNumber(i)}}
        </td>
      </ng-container>

      <ng-container matColumnDef="route">
        <td mat-cell *matCellDef="let route">
          <kpn-subset-orphan-route [route]="route"></kpn-subset-orphan-route>
        </td>
      </ng-container>

      <tr mat-row *matRowDef="let route; columns: displayedColumns;"></tr>

    </table>

    <!--    <kpn-paginator-->
    <!--      [length]="dataSource.data.length"-->
    <!--      [pageIndex]="0"-->
    <!--    </kpn-paginator>-->
  `,
  styles: [`

    table {
      width: 100%;
    }

    .mat-column-rowNumber {
      width: 50px;
      vertical-align: top;
      padding-top: 15px;
    }

    td.mat-cell:first-of-type {
      padding-left: 10px;
    }

  `]
})
export class SubsetOrphanRoutesTableComponent implements OnInit {

  @Input() timeInfo: TimeInfo;
  @Input() orphanRoutes: RouteSummary[];

  @ViewChild(PaginatorComponent, {static: true}) paginator: PaginatorComponent;

  dataSource: MatTableDataSource<RouteSummary>;

  displayedColumns = ['rowNumber', 'route'];

  private readonly filterCriteria = new BehaviorSubject(new SubsetOrphanRouteFilterCriteria());

  constructor(private subsetOrphanRoutesService: SubsetOrphanRoutesService) {
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    this.dataSource.paginator = this.paginator.matPaginator;
    this.filterCriteria.subscribe(criteria => {
      const filter = new SubsetOrphanRouteFilter(this.timeInfo, criteria, this.filterCriteria);
      this.dataSource.data = filter.filter(this.orphanRoutes);
      this.subsetOrphanRoutesService.filterOptions.next(filter.filterOptions(this.orphanRoutes));
    });
  }

  rowNumber(index: number): number {
    return this.paginator.rowNumber(index);
  }

}
