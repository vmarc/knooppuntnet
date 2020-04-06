import {Component} from "@angular/core";
import {Input} from "@angular/core";
import {Output} from "@angular/core";
import {EventEmitter} from "@angular/core";
import {ElementRef} from "@angular/core";
import {ChangeDetectorRef} from "@angular/core";
import {ViewChild} from "@angular/core";
import {AfterViewInit} from "@angular/core";
import {PageEvent} from "@angular/material/paginator";
import {MatPaginator} from "@angular/material/paginator";
import {PaginatorService} from "./paginator.service";

@Component({
  selector: "kpn-paginator",
  template: `

    <div *ngIf="!isTranslationsUpdated()">
      <span id="itemsPerPageLabel" i18n="@@paginator.items-per-page-label">Items per page</span>
      <span id="nextPageLabel" i18n="@@paginator.next-page-label">Next page</span>
      <span id="previousPageLabel" i18n="@@paginator.previous-page-label">Previous page</span>
      <span id="firstPageLabel" i18n="@@paginator.first-page-label">First page</span>
      <span id="lastPageLabel" i18n="@@paginator.last-page-label">Last page</span>
      <span id="of" i18n="@@paginator.from">of</span>
    </div>

    <mat-paginator
      (page)="page.emit($event)"
      [pageIndex]="pageIndex"
      [pageSize]="pageSize"
      [pageSizeOptions]="pageSizeOptions"
      [length]="length"
      [showFirstLastButtons]="showFirstLastButtons"
      [hidePageSize]="hidePageSize">
    </mat-paginator>
  `
})
export class PaginatorComponent implements AfterViewInit {

  @Input() pageIndex: number;
  @Input() pageSize: number;
  @Input() pageSizeOptions: Array<number>;
  @Input() length: number;
  @Input() showFirstLastButtons: boolean;
  @Input() hidePageSize: boolean;

  @Output() page = new EventEmitter<PageEvent>();

  @ViewChild(MatPaginator, {static: true}) matPaginator: MatPaginator;

  constructor(private paginatorService: PaginatorService,
              private element: ElementRef,
              private cdr: ChangeDetectorRef) {
  }

  ngAfterViewInit(): void {
    const divElement = this.element.nativeElement.children[0];
    if (divElement != null) {
      if (!this.paginatorService.isTranslationsUpdated()) {
        const elements = divElement.children;
        this.paginatorService.updateTranslations(elements);
        this.initTranslations();
        this.cdr.detectChanges();
      } else {
        this.initTranslations();
      }
    }
  }

  isTranslationsUpdated() {
    return this.paginatorService.isTranslationsUpdated();
  }

  rowNumber(index: number): number {
    return (this.matPaginator.pageIndex * this.matPaginator.pageSize) + index + 1;
  }

  private initTranslations(): void {
    this.matPaginator._intl.itemsPerPageLabel = this.paginatorService.itemsPerPageLabel;
    this.matPaginator._intl.nextPageLabel = this.paginatorService.nextPageLabel;
    this.matPaginator._intl.previousPageLabel = this.paginatorService.previousPageLabel;
    this.matPaginator._intl.firstPageLabel = this.paginatorService.firstPageLabel;
    this.matPaginator._intl.lastPageLabel = this.paginatorService.lastPageLabel;
    this.matPaginator._intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
      if (length === 0 || pageSize === 0) {
        return `0 ${this.paginatorService.of} ${length}`;
      }
      const itemCount = Math.max(length, 0);
      const startIndex = page * pageSize;
      const endIndex = startIndex < itemCount ?
        Math.min(startIndex + pageSize, itemCount) :
        startIndex + pageSize;
      return `${startIndex + 1} - ${endIndex} ${this.paginatorService.of} ${length}`;
    };
  }
}
