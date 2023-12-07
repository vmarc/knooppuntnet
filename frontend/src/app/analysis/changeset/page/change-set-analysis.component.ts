import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { ChangeSetPage } from '@api/common/changes';
import { IconInvestigateComponent } from '@app/components/shared/icon';
import { IconHappyComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-change-set-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      @if (page.summary.happy) {
        <kpn-icon-happy />
        <span i18n="@@change-set.header.analysis.happy">
          This changeset brought improvements.
        </span>
      } @else if (page.summary.investigate) {
        <kpn-icon-investigate />
        <span i18n="@@change-set.header.analysis.investigate">
          Maybe this changeset is worth a closer look.
        </span>
      } @else {
        <span i18n="@@change-set.header.analysis.no-impact">
          The changes do not seem to have an impact on the analysis result.
        </span>
      }
    </div>
  `,
  standalone: true,
  imports: [IconHappyComponent, IconInvestigateComponent],
})
export class ChangeSetAnalysisComponent {
  @Input() page: ChangeSetPage;
}
