import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Fact} from "../../../kpn/shared/fact";

@Component({
  selector: "kpn-fact-comma-list",
  template: `
    <div *ngIf="hasFacts()">
      {{title}}:
      <div class="kpn-comma-list">
        <div *ngFor="let fact of facts">
          {{fact.name}}
        </div>
      </div>
    </div>
  `
})
export class FactCommaListComponent {

  @Input() title: string;
  @Input() facts: List<Fact>;

  hasFacts(): boolean {
    return this.facts && !this.facts.isEmpty();
  }

}
