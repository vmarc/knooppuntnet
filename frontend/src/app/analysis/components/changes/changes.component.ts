import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ChangesService } from '@app/analysis/components/changes/changes.service';
import { ChangeFilterComponent } from '@app/analysis/components/changes/filter/change-filter.component';
import { ListComponent } from '@app/shared/components/list/list.component';
import { SwitchComponent } from '@app/shared/components/switch/switch.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { ChangeOption } from '@app/shared/kpn/common/change-option';

@Component({
  selector: 'ui-changes',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (changeCount() === 0) {
      <div i18n="@@changes.no-changes">No changes</div>
    }

    @if (changeCount() > 0) {
      <p>
        <ui-situation-on [timestamp]="situationOn()" />
      </p>

      <ui-switch
        i18n-label="@@changes.impact"
        label="impact"
        [value]="impact()"
        (valueChange)="impactChanged($event)"
      />
      <ui-list
        [pageIndex]="pageIndex()"
        (pageIndexChange)="onPageIndexChange($event)"
        [pageSize]="pageSize()"
        (pageSizeChange)="onPageSizeChange($event)"
        [length]="changeCount()"
        [filter]="true"
      >
        <ui-change-filter
          [filterOptions]="filterOptions()"
          (optionSelected)="onOptionSelected($event)"
          filter
        />
        <ng-content />
      </ui-list>
    }
  `,
  imports: [SwitchComponent, ChangeFilterComponent, ListComponent, SituationOnComponent],
})
export class ChangesComponent {
  readonly service = input.required<ChangesService>();

  readonly situationOn = computed(() => this.service().situationOn());
  readonly impact = computed(() => this.service().impact());
  readonly pageSize = computed(() => this.service().pageSize());
  readonly pageIndex = computed(() => this.service().pageIndex());
  readonly filterOptions = computed(() => this.service().filterOptions());
  readonly changeCount = computed(() => this.service().changeCount());

  impactChanged(impact: boolean): void {
    this.service().updateImpact(impact);
  }

  onPageIndexChange(pageIndex: number): void {
    window.scroll(0, 0);
    this.service().updatePageIndex(pageIndex);
  }

  onPageSizeChange(pageSize: number): void {
    this.service().updatePageSize(pageSize);
  }

  onOptionSelected(option: ChangeOption): void {
    this.service().updateFilterOption(option);
  }
}
