import {Component, Input} from "@angular/core";
import {FactDiffs} from "../../../kpn/shared/diff/common/fact-diffs";

@Component({
  selector: "kpn-fact-diffs",
  template: `
    <div *ngIf="!!factDiffs">
      <kpn-fact-comma-list [title]="'Resolved facts'" [facts]="factDiffs.resolved"></kpn-fact-comma-list> <!-- "Opgeloste feiten" -->
      <kpn-fact-comma-list [title]="'Introduced facts'" [facts]="factDiffs.introduced"></kpn-fact-comma-list> <!-- "Nieuwe feiten" -->
      <kpn-fact-comma-list [title]="'Remaining facts'" [facts]="factDiffs.remaining"></kpn-fact-comma-list> <!-- "Overblijvende feiten" -->
    </div>
  `
})
export class FactDiffsComponent {
  @Input() factDiffs: FactDiffs;
}
