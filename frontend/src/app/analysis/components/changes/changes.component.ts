import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { PaginatorComponent } from '@app/shared/components/paginator/paginator.component';

@Component({
  selector: 'kpn-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-slide-toggle
      [checked]="impact()"
      (change)="onImpactChanged($event)"
      i18n="@@changes.impact"
      >Impact
    </mat-slide-toggle>

    <kpn-paginator
      [pageIndex]="pageIndex()"
      (pageIndexChange)="onPageIndexChange($event)"
      [pageSize]="pageSize()"
      (pageSizeChange)="onPageSizeChange($event)"
      [length]="totalCount()"
      [showPageSizeSelection]="true"
    />

    @if (totalCount() === 0) {
      <div i18n="@@changes.no-changes">No changes</div>
    }

    @if (changeCount() > 0) {
      <div>
        <ng-content></ng-content>
      </div>
    }
  `,
  imports: [MatSlideToggleModule, PaginatorComponent],
})
export class ChangesComponent {
  changeCount = input.required<number>();
  totalCount = input.required<number>();
  impact = input.required<boolean>();
  pageSize = input.required<number>();
  pageIndex = input.required<number>();

  impactChange = output<boolean>();
  pageSizeChange = output<number>();
  pageIndexChange = output<number>();

  onImpactChanged(event: MatSlideToggleChange) {
    this.impactChange.emit(event.checked);
  }

  onPageIndexChange(pageIndex: number) {
    window.scroll(0, 0);
    this.pageIndexChange.emit(pageIndex);
  }

  onPageSizeChange(pageSize: number) {
    this.pageSizeChange.emit(pageSize);
  }
}
