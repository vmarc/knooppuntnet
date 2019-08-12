import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";

@Component({
  selector: "kpn-change-set-analysis",
  template: `
    <div>
      change-set-analysis
    </div>
  `
})
export class ChangeSetAnalysisComponent {
  @Input() page: ChangeSetPage;
}
