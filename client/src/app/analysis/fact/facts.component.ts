import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Fact} from "../../kpn/shared/fact";
import {Facts} from "./facts";

@Component({
  selector: "kpn-facts",
  template: `

    <div *ngIf="facts.isEmpty()">
      None <!-- Geen -->
    </div>    
    
    <div *ngFor="let fact of facts" class="fact">
      <p>
        <kpn-fact-level [factLevel]="factLevel(fact.name)" class="level"></kpn-fact-level>
        <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
      </p>
      <p>
        <kpn-fact-description [factName]="fact.name" class="description"></kpn-fact-description>
      </p>

    </div>
  `,
  styles: [`

    .fact {
      margin-top: 15px;
    }

    .level {
      display: inline-block;
      width: 25px;
    }

    .description {
      padding-left: 25px;
      padding-bottom: 3px;
      font-style: italic;
    }

    .reference {
      display: inline-block;
      padding-left: 20px;
    }

  `]
})
export class FactsComponent {

  @Input() facts: List<Fact>

  factLevel(factName: string): string {
    return Facts.factLevels.get(factName, "unknown");
  }

}
