import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { FactInfo } from './fact-info';
import { FactLevel } from './fact-level';
import { Facts } from './facts';

@Component({
  selector: 'kpn-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="filteredFactInfos.length === 0" i18n="@@facts.none">None</div>

    <div *ngFor="let factInfo of filteredFactInfos" class="fact">
      <div>
        <kpn-fact-level [factLevel]="factLevel(factInfo)" class="level" />
        <kpn-fact-name [fact]="factInfo.fact"></kpn-fact-name>
        <div *ngIf="factInfo.networkRef" class="reference">
          <kpn-brackets>
            <a
              class="text"
              [routerLink]="'/analysis/network/' + factInfo.networkRef.id"
            >{{ factInfo.networkRef.name }}</a
            >
          </kpn-brackets>
        </div>
        <div *ngIf="factInfo.routeRef" class="reference">
          <kpn-brackets>
            <kpn-link-route
              [routeId]="factInfo.routeRef.id"
              [title]="factInfo.routeRef.name"
            />
          </kpn-brackets>
        </div>
        <div *ngIf="factInfo.nodeRef" class="reference">
          <kpn-brackets>
            <kpn-link-node
              [nodeId]="factInfo.nodeRef.id"
              [nodeName]="factInfo.nodeRef.name"
            />
          </kpn-brackets>
        </div>
      </div>
      <div class="description">
        <kpn-fact-description [factInfo]="factInfo"/>
      </div>
    </div>
  `,
  styles: [
    `
      .fact {
        margin-top: 15px;
      }

      .level {
        display: inline-block;
        width: 25px; /* level-width */
      }

      .description {
        display: inline-block;
        padding-left: 25px; /* level-width */
        padding-bottom: 10px;
        font-style: italic;
        max-width: 60em;
      }

      .reference {
        display: inline-block;
        padding-left: 20px;
      }
    `,
  ],
})
export class FactsComponent {
  @Input() factInfos: FactInfo[];

  get filteredFactInfos(): FactInfo[] {
    return this.factInfos.filter((factInfo) => factInfo.fact !== 'RouteBroken');
  }

  factLevel(factInfo: FactInfo): FactLevel {
    return Facts.factLevels.get(factInfo.fact);
  }
}
