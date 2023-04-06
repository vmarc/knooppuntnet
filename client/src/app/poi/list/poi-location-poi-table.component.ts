import { ChangeDetectionStrategy } from '@angular/core';
import { OnChanges } from '@angular/core';
import { SimpleChanges } from '@angular/core';
import { ViewChild } from '@angular/core';
import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { LocationPoiInfo } from '@api/common/poi/location-poi-info';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidthService } from '@app/components/shared/page-width.service';
import { PaginatorComponent } from '@app/components/shared/paginator/paginator.component';
import { actionPreferencesPageSize } from '@app/core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '@app/core/preferences/preferences.selectors';
import { actionLocationPoisPageIndex } from '../store/poi.actions';
import { selectLocationPoisPageIndex } from '../store/poi.selectors';

@Component({
  selector: 'kpn-poi-location-poi-table',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-paginator
      [pageIndex]="pageIndex$ | async"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="poiCount"
      [showFirstLastButtons]="false"
      [showPageSizeSelection]="true"
    />

    <table mat-table [dataSource]="dataSource">
      <ng-container matColumnDef="nr">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-pois.table.nr">
          Nr
        </th>
        <td mat-cell *matCellDef="let poi">{{ poi.rowIndex + 1 }}</td>
      </ng-container>

      <ng-container matColumnDef="layer">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-pois.table.layer"
        >
          Layer
        </th>
        <td mat-cell *matCellDef="let poi">
          <span *ngFor="let layer of poi.layers">
            {{ layer }}
          </span>
        </td>
      </ng-container>

      <ng-container matColumnDef="id">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-pois.table.id">
          Id
        </th>
        <td mat-cell *matCellDef="let poi">
          <a [routerLink]="'/poi/' + poi.elementType + '/' + poi.elementId">{{
            poi._id
          }}</a>
        </td>
      </ng-container>

      <ng-container matColumnDef="description">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-pois.table.description"
        >
          Description
        </th>
        <td mat-cell *matCellDef="let poi">
          {{ poi.description }}
        </td>
      </ng-container>

      <ng-container matColumnDef="address">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-pois.table.address"
        >
          Address
        </th>
        <td mat-cell *matCellDef="let poi">
          {{ poi.address }}
        </td>
      </ng-container>

      <ng-container matColumnDef="link">
        <th mat-header-cell *matHeaderCellDef i18n="@@location-pois.table.link">
          Link
        </th>
        <td mat-cell *matCellDef="let poi">
          {{ poi.link ? 'yes' : '' }}
        </td>
      </ng-container>

      <ng-container matColumnDef="image">
        <th
          mat-header-cell
          *matHeaderCellDef
          i18n="@@location-pois.table.image"
        >
          Image
        </th>
        <td mat-cell *matCellDef="let poi">
          {{ poi.image ? 'yes' : '' }}
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns$ | async"></tr>
      <tr
        mat-row
        *matRowDef="let node; columns: displayedColumns$ | async"
      ></tr>
    </table>

    <kpn-paginator
      [pageIndex]="pageIndex$ | async"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize$ | async"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="poiCount"
    />
  `,
  styles: [
    `
      .mat-column-nr {
        flex: 0 0 4em;
      }
    `,
  ],
})
export class PoiLocationPoiTableComponent implements OnInit, OnChanges {
  @Input() pois: LocationPoiInfo[];
  @Input() poiCount: number;

  @ViewChild(PaginatorComponent, { static: true })
  paginator: PaginatorComponent;

  readonly pageSize$ = this.store.select(selectPreferencesPageSize);
  readonly pageIndex$ = this.store.select(selectLocationPoisPageIndex);

  dataSource: MatTableDataSource<LocationPoiInfo>;
  displayedColumns$: Observable<Array<string>>;

  constructor(
    private pageWidthService: PageWidthService,
    private store: Store
  ) {
    this.dataSource = new MatTableDataSource();
    this.displayedColumns$ = pageWidthService.current$.pipe(
      map(() => this.displayedColumns())
    );
  }

  ngOnInit(): void {
    this.dataSource.data = this.pois;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['pois']) {
      this.dataSource.data = this.pois;
    }
  }

  onPageSizeChange(pageSize: number) {
    this.store.dispatch(actionPreferencesPageSize({ pageSize }));
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.store.dispatch(actionLocationPoisPageIndex({ pageIndex }));
  }

  private displayedColumns() {
    if (this.pageWidthService.isVeryLarge()) {
      return ['nr', 'layer', 'id', 'description', 'address', 'link', 'image'];
    }

    if (this.pageWidthService.isLarge()) {
      return ['nr', 'layer', 'id', 'description', 'address', 'link', 'image'];
    }

    return ['nr', 'layer', 'id', 'description', 'address', 'link', 'image'];
  }
}
