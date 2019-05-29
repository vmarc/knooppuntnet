import {Component} from "@angular/core";
import {Facts} from "../../fact/facts";

@Component({
  selector: "kpn-facts-page",
  template: `
    <h1>
      All facts
    </h1>

    <kpn-items>
      <kpn-item *ngFor="let factName of allFactNames(); let i=index" index="{{i}}">
        <p>
          <kpn-fact-name [factName]="factName"></kpn-fact-name>
        </p>
        <p>
          <kpn-fact-description [factName]="factName"></kpn-fact-description>
        </p>
      </kpn-item>
    </kpn-items>
  `
})
export class FactsPageComponent {

  allFactNames() {
    return Facts.allFactNames;
  }

}
