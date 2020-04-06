import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Fact} from "../../../kpn/api/custom/fact";

@Component({
  selector: "kpn-fact-comma-list",
  template: `
    <div *ngIf="hasFacts()" class="kpn-detail kpn-line">
      {{title}}:&nbsp;
      <div class="kpn-comma-list">
        <span *ngFor="let fact of facts">
          <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
        </span>
      </div>
      <kpn-icon-happy *ngIf="icon == 'happy'"></kpn-icon-happy>
      <kpn-icon-investigate *ngIf="icon == 'investigate'"></kpn-icon-investigate>
    </div>
  `
})
export class FactCommaListComponent {

  @Input() title: string;
  @Input() facts: List<Fact>;
  @Input() icon: string;

  hasFacts(): boolean {
    return this.facts && !this.facts.isEmpty();
  }

}
