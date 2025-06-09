import { output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { OldPaginatorComponent } from '@app/shared/components/paginator/old-paginator.component';

@Component({
  selector: 'ui-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-slide-toggle
      [checked]="impact()"
      (change)="onImpactChanged($event)"
      i18n="@@changes.impact"
      >Impact
    </mat-slide-toggle>

    <ui-old-paginator
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
        <ng-content />
      </div>
    }
  `,
  imports: [MatSlideToggleModule, OldPaginatorComponent],
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
