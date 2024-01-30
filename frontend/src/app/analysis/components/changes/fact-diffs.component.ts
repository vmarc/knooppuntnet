import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { FactDiffs } from '@api/common/diff/common';
import { FactNameComponent } from '@app/analysis/fact';
import { IconHappyComponent } from '@app/components/shared/icon';
import { IconInvestigateComponent } from '@app/components/shared/icon';

@Component({
  selector: 'kpn-fact-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (factDiffs()) {
      @if (factDiffs().resolved.length > 0) {
        <div class="kpn-detail kpn-line">
          @if (factDiffs().resolved.length === 1) {
            <div>
              <span i18n="@@fact-diffs.resolved-fact" class="kpn-label">Resolved fact</span>
              <kpn-fact-name [fact]="factDiffs().resolved[0]" />
            </div>
          }
          @if (factDiffs().resolved.length > 1) {
            <div>
              <span i18n="@@fact-diffs.resolved-facts" class="kpn-label">Resolved facts</span>
              <div class="kpn-comma-list">
                @for (fact of factDiffs().resolved; track $index) {
                  <span>
                    <kpn-fact-name [fact]="fact" />
                  </span>
                }
              </div>
            </div>
          }
          <kpn-icon-happy />
        </div>
      }

      @if (factDiffs().introduced.length > 0) {
        <div class="kpn-detail kpn-line">
          @if (factDiffs().introduced.length === 1) {
            <div>
              <span i18n="@@fact-diffs.introduced-fact" class="kpn-label">Introduced fact</span>
              <kpn-fact-name [fact]="factDiffs().introduced[0]" />
            </div>
          }
          @if (factDiffs().introduced.length > 1) {
            <div>
              <span i18n="@@fact-diffs.introduced-facts" class="kpn-label">Introduced facts</span>
              <div class="kpn-comma-list">
                @for (fact of factDiffs().introduced; track $index) {
                  <span>
                    <kpn-fact-name [fact]="fact" />
                  </span>
                }
              </div>
            </div>
          }
          <kpn-icon-investigate />
        </div>
      }

      @if (factDiffs().remaining.length > 0) {
        <div class="kpn-detail kpn-line">
          @if (factDiffs().remaining.length === 1) {
            <div>
              <span i18n="@@fact-diffs.remaining-fact" class="kpn-label">Remaining fact</span>
              <kpn-fact-name [fact]="factDiffs().remaining[0]" />
            </div>
          }
          @if (factDiffs().remaining.length > 1) {
            <div>
              <span i18n="@@fact-diffs.remaining-facts" class="kpn-label">Remaining facts</span>
              <div class="kpn-comma-list">
                @for (fact of factDiffs().remaining; track $index) {
                  <span>
                    <kpn-fact-name [fact]="fact" />
                  </span>
                }
              </div>
            </div>
          }
        </div>
      }
    }
  `,
  standalone: true,
  imports: [FactNameComponent, IconHappyComponent, IconInvestigateComponent],
})
export class FactDiffsComponent {
  factDiffs = input.required<FactDiffs>();
}
