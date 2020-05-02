import {ChangeDetectionStrategy} from "@angular/core";
import {Input} from "@angular/core";
import {Component} from "@angular/core";
import {List} from "immutable";
import {LocationFact} from "../../../kpn/api/common/location/location-fact";
import {Fact} from "../../../kpn/api/custom/fact";
import {FactLevel} from "../../fact/fact-level";
import {Facts} from "../../fact/facts";

@Component({
  selector: "kpn-location-facts",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="locationFacts.isEmpty()" i18n="@@location-facts.none" class="none">
      None
    </div>

    <kpn-items>
      <kpn-item *ngFor="let locationFact of locationFacts; let i=index" [index]="i">
        <div class="kpn-line">
          <kpn-fact-name [factName]="locationFact.fact.name"></kpn-fact-name>
          <kpn-brackets>{{locationFact.refs.size}}</kpn-brackets>
          <kpn-fact-level [factLevel]="factLevel(locationFact.fact)" class="level"></kpn-fact-level>
        </div>
        <div class="description">
          <kpn-fact-description [factName]="locationFact.fact.name"></kpn-fact-description>
        </div>
        <div *ngIf="locationFact.elementType == 'route'" class="elements kpn-comma-list">
          <kpn-link-route
            *ngFor="let ref of locationFact.refs"
            [routeId]="ref.id"
            [title]="ref.name">
          </kpn-link-route>
        </div>
        <div *ngIf="locationFact.elementType == 'node'" class="elements kpn-comma-list">
          <kpn-link-node
            *ngFor="let ref of locationFact.refs"
            [nodeId]="ref.id"
            [nodeName]="ref.name">
          </kpn-link-node>
        </div>
      </kpn-item>
    </kpn-items>
  `,
  styles: [`

    .none {
      padding-top: 50px;
    }

    .description {
      max-width: 60em;
      border-left: 1px solid lightgray;
      padding-left: 20px;
    }

    .elements {
      max-width: 60em;
      padding-right: 20px;
    }
  `]
})
export class LocationFactsComponent {

  @Input() locationFacts: List<LocationFact>;

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact.name);
  }

}
