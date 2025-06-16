import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';
import { NzSwitchComponent } from 'ng-zorro-antd/switch';

@Component({
  selector: 'ui-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="header">
      <div class="filter-switch kpn-line" (click)="onToggleImpact()">
        <nz-switch nzSize="small" [ngModel]="impact()" />
        <span i18n="@@changes.impact">Impact</span>
      </div>
      <div class="paginator">
        <ui-paginator
          [pageIndex]="pageIndex()"
          (pageIndexChange)="onPageIndexChange($event)"
          [pageSize]="pageSize()"
          (pageSizeChange)="onPageSizeChange($event)"
          [length]="totalCount()"
        />
      </div>
    </div>

    @if (totalCount() === 0) {
      <div i18n="@@changes.no-changes">No changes</div>
    }

    @if (changeCount() > 0) {
      <div>
        <ng-content />
      </div>
    }
  `,
  styles: `
    .header {
      display: flex;
    }
    .paginator {
      margin-left: auto;
    }
  `,
  imports: [MatSlideToggleModule, NzSwitchComponent, FormsModule, PaginatorComponent],
})
export class ChangesComponent {
  readonly changeCount = input.required<number>();
  readonly totalCount = input.required<number>();
  readonly impact = input.required<boolean>();
  readonly pageSize = input.required<number>();
  readonly pageIndex = input.required<number>();

  readonly impactChange = output<boolean>();
  readonly pageSizeChange = output<number>();
  readonly pageIndexChange = output<number>();

  onToggleImpact() {
    this.impactChange.emit(!this.impact());
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.pageIndexChange.emit(pageIndex);
  }

  onPageSizeChange(pageSize: number) {
    this.pageSizeChange.emit(pageSize);
  }
}
