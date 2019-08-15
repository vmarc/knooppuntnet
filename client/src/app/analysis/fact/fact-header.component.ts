import {Component, Input, OnInit} from "@angular/core";
import {FactLevel} from "./fact-level";
import {Facts} from "./facts";

@Component({
  selector: "kpn-fact-header",
  template: `
    <div class="kpn-line">
      <span class="kpn-thick">
        <kpn-fact-name [factName]="factName"></kpn-fact-name>
      </span>
      <span *ngIf="factCount !== null">
        ({{factCount}})
      </span>
      <kpn-fact-level [factLevel]="factLevel(factName)" class="level"></kpn-fact-level>
    </div>
    <kpn-fact-description [factName]="factName"></kpn-fact-description>
  `,
  styles: []
})
export class FactHeaderComponent {

  @Input() factName: string;
  @Input() factCount: number = null;

  factLevel(factName: string): FactLevel {
    return Facts.factLevels.get(factName);
  }

}
