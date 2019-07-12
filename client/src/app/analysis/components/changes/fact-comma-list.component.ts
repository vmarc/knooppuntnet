import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {Fact} from "../../../kpn/shared/fact";

@Component({
  selector: "kpn-fact-comma-list",
  template: `
    <div *ngIf="hasFacts()" class="kpn-detail kpn-line">
      {{title}}:&nbsp;
      <div class="kpn-comma-list">
        <div *ngFor="let fact of facts">
          {{fact.name}}
        </div>
      </div>
      <mat-icon *ngIf="icon == 'happy'" svgIcon="happy"></mat-icon>
      <mat-icon *ngIf="icon == 'investigate'" svgIcon="investigate"></mat-icon>
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
