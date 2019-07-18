import {Component, Input} from "@angular/core";
import {FactDiffs} from "../../../kpn/shared/diff/common/fact-diffs";

@Component({
  selector: "kpn-fact-diffs",
  template: `
    <div *ngIf="!!factDiffs">

      <div *ngIf="!factDiffs.resolved.isEmpty()" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.resolved.size == 1">
          <span i18n="@@fact-diffs.resolved-fact">Resolved fact</span>:
          {{factDiffs.resolved.get(0).name}}
        </div>
        <div *ngIf="factDiffs.resolved.size > 1">
          <span i18n="@@fact-diffs.resolved-facts">Resolved facts</span>:
          <div class="kpn-comma-list">
            <div *ngFor="let fact of factDiffs.resolved">
              {{fact.name}}
            </div>
          </div>
        </div>
        <mat-icon svgIcon="happy"></mat-icon>
      </div>


      <div *ngIf="!factDiffs.introduced.isEmpty()" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.introduced.size == 1">
          <span i18n="@@fact-diffs.introduced-fact">Introduced fact</span>:
          {{factDiffs.introduced.get(0).name}}
        </div>
        <div *ngIf="factDiffs.introduced.size > 1">
          <span i18n="@@fact-diffs.introduced-facts">Introduced facts</span>:
          <div class="kpn-comma-list">
            <div *ngFor="let fact of factDiffs.introduced">
              {{fact.name}}
            </div>
          </div>
        </div>
        <mat-icon svgIcon="investigate"></mat-icon>
      </div>


      <div *ngIf="!factDiffs.remaining.isEmpty()" class="kpn-detail kpn-line">
        <div *ngIf="factDiffs.remaining.size == 1">
          <span i18n="@@fact-diffs.remaining-fact">Remaining fact</span>:
          {{factDiffs.remaining.get(0).name}}
        </div>
        <div *ngIf="factDiffs.remaining.size > 1">
          <span i18n="@@fact-diffs.remaining-facts">Remaining facts</span>:
          <div class="kpn-comma-list">
            <div *ngFor="let fact of factDiffs.remaining">
              {{fact.name}}
            </div>
          </div>
        </div>
      </div>

    </div>
  `
})
export class FactDiffsComponent {
  @Input() factDiffs: FactDiffs;
}
