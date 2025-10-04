import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { OldPoiService } from '@app/shared/services/old-poi.service';
import { ChangeDetectionStrategy } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';

@Component({
  selector: 'ui-poi-group',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  changeDetection: ChangeDetectionStrategy.Default,
  template: `
    <nz-collapse>
      <nz-collapse-panel [nzHeader]="groupHeader" [nzActive]="false">
        <ng-template #groupHeader>
          <label
            nz-checkbox
            (click)="$event.stopPropagation()"
            [nzChecked]="isEnabled()"
            (nzCheckedChange)="groupEnabledChanged($event)"
          ></label>
          <span class="title">{{ title() }}</span>
          <span class="kpn-thin">(10/10)</span>
        </ng-template>

        <div></div>

        <div>
          <button nz-button (click)="showAllClicked()" i18n="@@planner.pois.show-all">
            Show all
          </button>
          <button nz-button (click)="hideAllClicked()" i18n="@@planner.pois.hide-all">
            Hide all
          </button>
          <button nz-button (click)="defaultClicked()" i18n="@@planner.pois.default">
            Default
          </button>
        </div>

        <div>
          <!-- eslint-disable @angular-eslint/template/i18n -->
          <div class="col-spacer"></div>
          <div class="col-level-0">NO</div>
          <div class="col-level-11">11</div>
          <div class="col-level-12">12</div>
          <div class="col-level-13">13</div>
          <div class="col-level-14">14</div>
          <div class="col-level-15">15</div>
          <!-- eslint-enable @angular-eslint/template/i18n -->
        </div>
        <ng-content />
      </nz-collapse-panel>
    </nz-collapse>
  `,
  styles: `
    .title {
      padding-left: 10px;
      padding-right: 20px;
    }
  `,
  imports: [
    MatExpansionModule,
    MatButtonModule,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    NzCheckboxComponent,
    NzButtonComponent,
  ],
})
export class PoiGroupComponent {
  readonly name = input.required<string>();
  readonly title = input.required<string>();

  private readonly poiService = inject(OldPoiService);

  isEnabled(): boolean {
    return this.poiService.isGroupEnabled(this.name());
  }

  groupEnabledChanged(checked: boolean) {
    this.poiService.updateGroupEnabled(this.name(), checked);
  }

  showAllClicked() {
    this.poiService.updateGroupShowAll(this.name());
  }

  hideAllClicked() {
    this.poiService.updateGroupHideAll(this.name());
  }

  defaultClicked() {
    this.poiService.updateGroupDefault(this.name());
  }
}
