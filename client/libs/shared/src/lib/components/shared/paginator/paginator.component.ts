import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { ViewChild } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'kpn-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-paginator
      (page)="pageChanged($event)"
      [pageIndex]="pageIndex"
      [pageSize]="pageSize"
      [pageSizeOptions]="[10, 25, 50, 100, 250, 500, 1000]"
      [length]="length"
      [showFirstLastButtons]="showFirstLastButtons"
      [hidePageSize]="!showPageSizeSelection"
    />
  `,
  styles: [
    `
      mat-paginator {
        background-color: transparent;
      }
    `,
  ],
  standalone: true,
  imports: [MatPaginatorModule],
})
export class PaginatorComponent implements AfterViewInit {
  @Input() pageSize: number;
  @Input() pageIndex: number;
  @Input() length: number;
  @Input() showFirstLastButtons = false;
  @Input() showPageSizeSelection = false;

  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() pageIndexChange = new EventEmitter<number>();

  @ViewChild(MatPaginator, { static: true }) matPaginator: MatPaginator;

  ngAfterViewInit(): void {
    this.initTranslations();
  }

  rowNumber(index: number): number {
    return this.matPaginator.pageSize * this.matPaginator.pageIndex + index + 1;
  }

  pageChanged(event: PageEvent): void {
    if (event.pageSize !== this.pageSize) {
      this.pageSizeChange.emit(event.pageSize);
    } else if (event.pageIndex !== event.previousPageIndex) {
      this.pageIndexChange.emit(event.pageIndex);
    }
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
