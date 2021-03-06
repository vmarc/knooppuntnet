import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginator } from '@angular/material/paginator';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';

@Component({
  selector: 'kpn-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-paginator
      (page)="itemsPerPageChanged($event)"
      [pageIndex]="pageIndex"
      [pageSize]="itemsPerPage$ | async"
      [pageSizeOptions]="[10, 25, 50, 100, 250, 500, 1000]"
      [length]="length"
      [showFirstLastButtons]="showFirstLastButtons"
      [hidePageSize]="!showPageSizeSelection"
    >
    </mat-paginator>
  `,
})
export class PaginatorComponent implements AfterViewInit {
  @Input() pageIndex: number;
  @Input() length: number;
  @Input() showFirstLastButtons = false;
  @Input() showPageSizeSelection = false;

  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(MatPaginator, { static: true }) matPaginator: MatPaginator;

  readonly itemsPerPage$ = this.store.select(selectPreferencesItemsPerPage);

  constructor(private element: ElementRef, private store: Store<AppState>) {}

  ngAfterViewInit(): void {
    this.initTranslations();
  }

  rowNumber(index: number): number {
    return this.matPaginator.pageIndex * this.matPaginator.pageSize + index + 1;
  }

  itemsPerPageChanged(event: PageEvent): void {
    this.store.dispatch(
      actionPreferencesItemsPerPage({ itemsPerPage: event.pageSize })
    );
    this.page.emit(event);
  }

  private initTranslations(): void {
    this.matPaginator._intl.itemsPerPageLabel = $localize`:@@paginator.items-per-page-label:Items per page`;
    this.matPaginator._intl.nextPageLabel = $localize`:@@paginator.next-page-label:Next page`;
    this.matPaginator._intl.previousPageLabel = $localize`:@@paginator.previous-page-label:Previous page`;
    this.matPaginator._intl.firstPageLabel = $localize`:@@paginator.first-page-label:First page`;
    this.matPaginator._intl.lastPageLabel = $localize`:@@paginator.last-page-label:Last page`;

    const of = $localize`:@@paginator.from:of`;
    this.matPaginator._intl.getRangeLabel = (
      page: number,
      pageSize: number,
      length: number
    ) => {
      if (length === 0 || pageSize === 0) {
        return `0 ${of} ${length}`;
      }
      const itemCount = Math.max(length, 0);
      const startIndex = page * pageSize;
      const endIndex =
        startIndex < itemCount
          ? Math.min(startIndex + pageSize, itemCount)
          : startIndex + pageSize;
      return `${startIndex + 1} - ${endIndex} ${of} ${length}`;
    };
  }
}
