import { NgTemplateOutlet } from '@angular/common';
import { signal } from '@angular/core';
import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { NzSwitchComponent } from 'ng-zorro-antd/switch';

@Component({
  selector: 'kpn-list',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (pageSize() > 0) {
      <div class="header">
        @if (filter() === true) {
          <div class="filter-switch" (click)="toggleFilterEnabled()">
            <nz-switch nzSize="small" [ngModel]="filterEnabled()" />
            <span>filter</span>
          </div>
        }
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

    @if (filterEnabled()) {
      <div class="split">
        <div class="split-left">
          <ng-content select="[filter]" />
        </div>
        <div class="split-right">
          <ng-container *ngTemplateOutlet="contents" />
        </div>
      </div>
    } @else {
      <ng-container *ngTemplateOutlet="contents" />
    }

    <ng-template #contents>
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
    </ng-template>
  `,
  styles: `
    .header {
      display: flex;
      align-items: center;
      padding-bottom: 0.5em;
      border-bottom: 1px solid lightgray;
    }

    .footer {
      display: flex;
      align-items: center;
      padding-top: 1em;
    }

    .paginator {
      margin-left: auto;
    }

    .filter-switch {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap: 0.3em;
      margin-right: 2em;
      cursor: pointer;
    }

    .filter-switch > nz-switch {
      padding-bottom: 2px;
    }

    .split {
      display: flex;
    }

    .split-left {
      border-right: 1px solid lightgray;
      margin-right: 1em;
      flex-grow: 0;
    }

    .split-right {
      flex-grow: 1;
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
  imports: [PaginatorComponent, NzSwitchComponent, FormsModule, NgTemplateOutlet],
})
export class ListComponent {
  pageSize = input<number>(0);
  pageIndex = input<number>(0);
  length = input<number>(0);
  filter = input<boolean>(false);

  pageSizeChange = output<number>();
  pageIndexChange = output<number>();

  filterEnabled = signal<boolean>(true);

  onPageSizeChange(pageSize: number): void {
    this.pageSizeChange.emit(pageSize);
  }

  onPageIndexChange(pageIndex: number): void {
    this.pageIndexChange.emit(pageIndex);
  }

  toggleFilterEnabled(): void {
    this.filterEnabled.set(!this.filterEnabled());
  }
}
