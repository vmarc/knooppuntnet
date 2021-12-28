import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { OnDestroy } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { Subscriptions } from '../../../util/Subscriptions';

@Component({
  selector: 'kpn-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-slide-toggle
      [checked]="impact$ | async"
      (change)="impactChanged($event)"
      i18n="@@changes.impact"
      >Impact
    </mat-slide-toggle>

    <kpn-paginator
      (page)="pageChanged($event)"
      [pageIndex]="pageIndex"
      [length]="totalCount"
      [showPageSizeSelection]="true"
    >
    </kpn-paginator>

    <div *ngIf="totalCount === 0" i18n="@@changes.no-changes">No changes</div>

    <div *ngIf="changeCount > 0">
      <ng-content></ng-content>

      <!--      <kpn-paginator-->
      <!--        (page)="pageChanged($event)"-->
      <!--        [pageIndex]="parameters.pageIndex"-->
      <!--        [length]="totalCount">-->
      <!--      </kpn-paginator>-->
    </div>
  `,
})
export class ChangesComponent implements OnDestroy {
  @Input() changeCount: number;
  @Input() totalCount: number;
  @Input() pageIndex: number;
  @Output() pageIndexChanged = new EventEmitter<number>();

  readonly impact$ = this.store.select(selectPreferencesImpact);

  private readonly subscriptions = new Subscriptions();

  constructor(private store: Store<AppState>) {}

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  impactChanged(event: MatSlideToggleChange) {
    this.store.dispatch(actionPreferencesImpact({ impact: event.checked }));
  }

  pageChanged(event: PageEvent) {
    window.scroll(0, 0);
    this.pageIndexChanged.emit(event.pageIndex);
  }
}
