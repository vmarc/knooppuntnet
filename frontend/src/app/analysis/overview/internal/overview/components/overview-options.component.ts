import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzTooltipDirective } from 'ng-zorro-antd/tooltip';
import { OverviewPageService } from '../overview-page.service';
import { OverviewFormat } from './overview-format';

@Component({
  selector: 'kpn-overview-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div i18n="@@overview.sidebar.display-options">Display options</div>

    <nz-radio-group [ngModel]="preferredFormat()" (ngModelChange)="formatChanged($event)">
      <li>
        <label
          nz-radio
          [nzValue]="optionAutomatic"
          nz-tooltip
          i18n-nzTooltipTitle="@@overview.sidebar.automatic.title"
          nzTooltipTitle="Use list or table format depending on page width"
          i18n="@@overview.sidebar.table.automatic"
        >
          Automatic selection
        </label>
      </li>
      <li>
        <label
          nz-radio
          [nzValue]="optionList"
          nz-tooltip
          i18n-nzTooltipTitle="@@overview.sidebar.list.title"
          nzTooltipTitle="Show overview in list format"
          i18n="@@overview.sidebar.list"
        >
          List
        </label>
      </li>
      <li>
        <label
          nz-radio
          [nzValue]="optionTable"
          nz-tooltip
          i18n-nzTooltipTitle="@@overview.sidebar.table.title"
          nzTooltipTitle="Show overview in table format"
          i18n="@@overview.sidebar.table"
        >
          Table
        </label>
      </li>
    </nz-radio-group>
  `,
  imports: [NzRadioComponent, NzRadioGroupComponent, FormsModule, NzTooltipDirective],
})
export class OverviewOptionsComponent {
  private readonly overviewService = inject(OverviewPageService);

  protected readonly optionList: OverviewFormat = 'list';
  protected readonly optionTable: OverviewFormat = 'table';
  protected readonly optionAutomatic: OverviewFormat = 'automatic';
  protected readonly preferredFormat = this.overviewService.preferredFormat;

  formatChanged(value: OverviewFormat) {
    this.overviewService.preferFormat(value);
  }
}
