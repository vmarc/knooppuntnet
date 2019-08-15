import {Component, Input} from "@angular/core";
import {NetworkFact} from "../../../kpn/shared/network-fact";
import {FactLevel} from "../../fact/fact-level";
import {Facts} from "../../fact/facts";

@Component({
  selector: "kpn-network-fact-header",
  template: `
    <div class="kpn-line">
      <span class="kpn-thick">
        <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
      </span>
      <span *ngIf="!fact.elements.isEmpty()">
        ({{fact.elements.size}})
      </span>
      <span *ngIf="!fact.elementIds.isEmpty()">
        ({{fact.elementIds.size}})
      </span>
      <span *ngIf="!fact.checks.isEmpty()">
        ({{fact.checks.size}})
      </span>
      <kpn-fact-level [factLevel]="factLevel()" class="level"></kpn-fact-level>
    </div>
    <div class="description">
      <kpn-fact-description [factName]="fact.name"></kpn-fact-description>
    </div>
  `,
  styles: [`
    .description {
      max-width: 60em;
    }
  `]
})
export class NetworkFactHeaderComponent {

  @Input() fact: NetworkFact;

  factLevel(): FactLevel {
    return Facts.factLevels.get(this.fact.name);
  }

}
