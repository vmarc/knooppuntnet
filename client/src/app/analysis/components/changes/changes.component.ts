import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';

@Component({
  selector: 'kpn-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-slide-toggle
      [checked]="impact"
      (change)="onImpactChanged($event)"
      i18n="@@changes.impact"
      >Impact
    </mat-slide-toggle>

    <kpn-paginator
      [pageIndex]="pageIndex"
      (pageIndexChanged)="onPageIndexChanged($event)"
      [itemsPerPage]="itemsPerPage"
      (itemsPerPageChanged)="onItemsPerPageChanged($event)"
      [length]="totalCount"
      [showPageSizeSelection]="true"
    >
    </kpn-paginator>

    <div *ngIf="totalCount === 0" i18n="@@changes.no-changes">No changes</div>

    <div *ngIf="changeCount > 0">
      <ng-content></ng-content>
    </div>
  `,
})
export class ChangesComponent {
  @Input() changeCount: number;
  @Input() totalCount: number;

  @Input() impact: boolean;
  @Input() itemsPerPage: number;
  @Input() pageIndex: number;

  @Output() impactChange = new EventEmitter<boolean>();
  @Output() itemsPerPageChange = new EventEmitter<number>();
  @Output() pageIndexChange = new EventEmitter<number>();

  onImpactChanged(event: MatSlideToggleChange) {
    this.impactChange.emit(event.checked);
  }

  onPageIndexChanged(pageIndex: number) {
    window.scroll(0, 0);
    this.pageIndexChange.emit(pageIndex);
  }

  onItemsPerPageChanged(itemsPerPage: number) {
    this.itemsPerPageChange.emit(itemsPerPage);
  }
}
