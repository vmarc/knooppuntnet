import { Signal } from '@angular/core';
import { ChangesFilterOption } from '@api/common/changes/filter/changes-filter-option';
import { Timestamp } from '@api/custom/timestamp';
import { ChangeOption } from '@app/shared/kpn/common/change-option';

export interface ChangesService {
  readonly situationOn: Signal<Timestamp>;
  readonly impact: Signal<boolean>;
  readonly pageSize: Signal<number>;
  readonly pageIndex: Signal<number>;
  readonly changeCount: Signal<number>;
  readonly filterOptions: Signal<ChangesFilterOption[]>;

  updatePageSize(pageSize: number): void;

  updateImpact(impact: boolean): void;

  updatePageIndex(pageIndex: number): void;

  updateFilterOption(option: ChangeOption): void;
}
