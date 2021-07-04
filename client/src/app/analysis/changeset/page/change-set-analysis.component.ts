import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes/change-set-page';

@Component({
  selector: 'kpn-change-set-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="isHappy()" class="kpn-line">
      <kpn-icon-happy></kpn-icon-happy>
      <span i18n="@@change-set.header.analysis.happy">
        This changeset brought improvements.
      </span>
    </div>

    <div *ngIf="isInvestigate()" class="kpn-line">
      <kpn-icon-investigate></kpn-icon-investigate>
      <span i18n="@@change-set.header.analysis.investigate">
        Maybe this changeset is worth a closer look.
      </span>
    </div>

    <div *ngIf="isNoImpact()" class="kpn-line">
      <span i18n="@@change-set.header.analysis.no-impact">
        The changes do not seem to have an impact on the analysis result.
      </span>
    </div>
  `,
})
export class ChangeSetAnalysisComponent {
  @Input() page: ChangeSetPage;

  isHappy() {
    return this.page.summary.happy;
  }

  isInvestigate() {
    return this.page.summary.investigate;
  }

  isNoImpact() {
    return !(this.isHappy() || this.isInvestigate());
  }
}
