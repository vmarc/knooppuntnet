import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {FactDiffs} from '@api/common/diff/common/fact-diffs';

@Component({
  selector: 'kpn-fact-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!!factDiffs">

      <div *ngIf="factDiffs.resolved.length > 0" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.resolved.length === 1">
          <span i18n="@@fact-diffs.resolved-fact" class="kpn-label">Resolved fact</span>
          <kpn-fact-name [factName]="factDiffs.resolved[0].name"></kpn-fact-name>
        </div>
        <div *ngIf="factDiffs.resolved.length > 1">
          <span i18n="@@fact-diffs.resolved-facts" class="kpn-label">Resolved facts</span>
          <div class="kpn-comma-list">
            <span *ngFor="let fact of factDiffs.resolved">
              <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
            </span>
          </div>
        </div>
        <kpn-icon-happy></kpn-icon-happy>
      </div>


      <div *ngIf="factDiffs.introduced.length > 0" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.introduced.length === 1">
          <span i18n="@@fact-diffs.introduced-fact" class="kpn-label">Introduced fact</span>
          <kpn-fact-name [factName]="factDiffs.introduced[0].name"></kpn-fact-name>
        </div>
        <div *ngIf="factDiffs.introduced.length > 1">
          <span i18n="@@fact-diffs.introduced-facts" class="kpn-label">Introduced facts</span>
          <div class="kpn-comma-list">
            <span *ngFor="let fact of factDiffs.introduced">
              <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
            </span>
          </div>
        </div>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>


      <div *ngIf="factDiffs.remaining.length > 0" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.remaining.length === 1">
          <span i18n="@@fact-diffs.remaining-fact" class="kpn-label">Remaining fact</span>
          <kpn-fact-name [factName]="factDiffs.remaining[0].name"></kpn-fact-name>
        </div>
        <div *ngIf="factDiffs.remaining.length > 1">
          <span i18n="@@fact-diffs.remaining-facts" class="kpn-label">Remaining facts</span>
          <div class="kpn-comma-list">
            <span *ngFor="let fact of factDiffs.remaining">
              <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
            </span>
          </div>
        </div>
      </div>

    </div>
  `
})
export class FactDiffsComponent {
  @Input() factDiffs: FactDiffs;
}
