import { computed } from '@angular/core';
import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { NzOptionComponent } from 'ng-zorro-antd/select';
import { NzSelectComponent } from 'ng-zorro-antd/select';

@Component({
  selector: 'ui-paginator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <span class="kpn-line paginator">
      @if (full()) {
        <span i18n="@@paginator.items-per-page-label">Items per page</span>
        <span>
          <nz-select [ngModel]="pageSizeString()" (ngModelChange)="pageSizeChanged($event)">
            <nz-option nzValue="10" nzLabel="10" />
            <nz-option nzValue="25" nzLabel="25" />
            <nz-option nzValue="50" nzLabel="50" />
            <nz-option nzValue="100" nzLabel="100" />
            <nz-option nzValue="250" nzLabel="250" />
            <nz-option nzValue="500" nzLabel="500" />
            <nz-option nzValue="1000" nzLabel="1000" />
          </nz-select>
        </span>
      }
      <span class="page-index-line">
        <span>{{ pageStartIndex() }}</span>
        <span>-</span>
        <span>{{ pageEndIndex() }}</span>
        <span i18n="@@paginator.from">of</span>
        <span>{{ length() }}</span>
      </span>
      @if (full()) {
        <span>
          <button nz-button nzShape="circle" [disabled]="buttonStartDisabled()" (click)="start()">
            <nz-icon nzType="double-left" />
          </button>
        </span>
      }
      <span>
        <button nz-button nzShape="circle" [disabled]="buttonPrevDisabled()" (click)="prev()">
          <nz-icon nzType="left" />
        </button>
      </span>
      <span>
        <button nz-button nzShape="circle" [disabled]="buttonNextDisabled()" (click)="next()">
          <nz-icon nzType="right" />
        </button>
      </span>
      @if (full()) {
        <span>
          <button nz-button nzShape="circle" [disabled]="buttonEndDisabled()" (click)="end()">
            <nz-icon nzType="double-right" />
          </button>
        </span>
      }
    </span>
  `,
  styles: `
    .paginator {
      font-size: 0.8em;
    }

    .page-index-line {
      display: flex;
      justify-content: flex-start;
      align-items: center;

      > :not(:last-child) {
        padding-right: 0.5em;
      }
    }
  `,
  imports: [FormsModule, NzButtonComponent, NzIconDirective, NzOptionComponent, NzSelectComponent],
})
export class PaginatorComponent {
  pageSize = input.required<number>();
  pageIndex = input.required<number>();
  length = input.required<number>();
  full = input<boolean>(true);

  pageSizeChange = output<number>();
  pageIndexChange = output<number>();

  pageSizeString = computed(() => '' + this.pageSize());
  pageStartIndex = computed(() => this.pageIndex() * this.pageSize() + 1);
  pageEndIndex = computed(() => {
    const endIndex = this.pageStartIndex() + this.pageSize() - 1;
    if (endIndex > this.length()) {
      return this.length();
    }
    return endIndex;
  });

  pageCount = computed(() => {
    const count = Math.trunc(this.length() / this.pageSize());
    if (count * this.pageSize() + 1 < this.length()) {
      return count + 1;
    }
    return count;
  });

  buttonStartDisabled = computed(() => this.length() > 0 && this.pageIndex() < 1);
  buttonPrevDisabled = computed(() => this.length() > 0 && this.pageIndex() < 1);
  buttonNextDisabled = computed(
    () => this.length() > 0 && this.pageIndex() === this.pageCount() - 1
  );
  buttonEndDisabled = computed(
    () => this.length() > 0 && this.pageIndex() === this.pageCount() - 1
  );

  pageSizeChanged(pageSize: number): void {
    this.pageSizeChange.emit(pageSize);
  }

  start(): void {
    this.pageIndexChange.emit(0);
  }

  prev(): void {
    this.pageIndexChange.emit(this.pageIndex() - 1);
  }

  next(): void {
    this.pageIndexChange.emit(this.pageIndex() + 1);
  }

  end(): void {
    this.pageIndexChange.emit(this.pageCount() - 1);
  }
}
