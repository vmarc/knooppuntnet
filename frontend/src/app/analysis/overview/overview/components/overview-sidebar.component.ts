import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { SidebarComponent } from '@app/components/shared/sidebar';
import { OverviewPageService } from '../overview-page.service';
import { OverviewFormat } from './overview-format';

@Component({
  selector: 'kpn-overview-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-sidebar>
      <div class="options">
        <div i18n="@@overview.sidebar.display-options" class="options-title">Display options</div>
        <mat-radio-group [value]="preferredFormat()" (change)="formatChanged($event)">
          <div>
            <mat-radio-button
              [value]="optionAutomatic"
              title="Use list or table format depending on page width"
              i18n-title="@@overview.sidebar.automatic.title"
              i18n="@@overview.sidebar.table.automatic"
            >
              Automatic selection
            </mat-radio-button>
          </div>
          <div>
            <mat-radio-button
              [value]="optionList"
              title="Show overview in list format"
              i18n-title="@@overview.sidebar.list.title"
              i18n="@@overview.sidebar.list"
            >
              List
            </mat-radio-button>
          </div>
          <div>
            <mat-radio-button
              [value]="optionTable"
              title="Show overview in table format"
              i18n-title="@@overview.sidebar.table.title"
              i18n="@@overview.sidebar.table"
            >
              Table
            </mat-radio-button>
          </div>
        </mat-radio-group>
      </div>
    </kpn-sidebar>
  `,
  styles: `
    .options {
      padding-top: 25px;
      padding-left: 25px;
      display: block;
    }

    .options-title {
      font-weight: bold;
      padding-bottom: 10px;
    }
  `,
  standalone: true,
  imports: [SidebarComponent, MatRadioModule, AsyncPipe],
})
export class OverviewSidebarComponent {
  private readonly overviewService = inject(OverviewPageService);

  protected readonly optionList: OverviewFormat = 'list';
  protected readonly optionTable: OverviewFormat = 'table';
  protected readonly optionAutomatic: OverviewFormat = 'automatic';
  protected readonly preferredFormat = this.overviewService.preferredFormat;

  formatChanged(event: MatRadioChange) {
    this.overviewService.preferFormat(event.value);
  }
}