import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';

@Component({
  selector: 'kpn-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (pageSize() > 0) {
      <div class="header">
        <ng-content select="[header-extra]" />
        <span class="paginator">
          <kpn-paginator
            [pageIndex]="pageIndex()"
            (pageIndexChange)="onPageIndexChange($event)"
            [pageSize]="pageSize()"
            (pageSizeChange)="onPageSizeChange($event)"
            [length]="length()"
          />
        </span>
      </div>
    }

    <div class="items">
      <ng-content />
    </div>

    @if (pageSize() > 0) {
      <div class="footer">
        <span class="paginator">
          <kpn-paginator
            [pageIndex]="pageIndex()"
            (pageIndexChange)="onPageIndexChange($event)"
            [pageSize]="pageSize()"
            (pageSizeChange)="onPageSizeChange($event)"
            [length]="length()"
            [full]="false"
          />
        </span>
      </div>
    }
  `,
  styles: `
    .header {
      display: flex;
      align-items: center;
    }

    .footer {
      display: flex;
      align-items: center;
      padding-top: 1em;
    }

    .paginator {
      margin-left: auto;
    }

    .items {
      margin-top: 20px;
      border-top-color: lightgray;
      border-top-style: solid;
      border-top-width: 1px;
    }

    @media (max-width: 768px) {
      /* media.maxWidth(PageWidth.SmallMaxWidth.px) */
      .items {
        margin-left: -20px;
        margin-right: -20px;
      }
    }
  `,
  imports: [PaginatorComponent],
})
export class ListComponent {
  pageSize = input<number>(0);
  pageIndex = input<number>(0);
  length = input<number>(0);

  pageSizeChange = output<number>();
  pageIndexChange = output<number>();

  onPageSizeChange(pageSize: number): void {
    this.pageSizeChange.emit(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.pageIndexChange.emit(pageIndex);
  }
}
