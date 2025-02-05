import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BracketsComponent } from '@app/shared/components/link/brackets.component';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { FactDescriptionComponent } from './fact-description.component';
import { FactInfo } from './fact-info';
import { FactLevel } from '@api/common/fact-level';
import { FactLevelComponent } from './fact-level.component';
import { FactNameComponent } from './fact-name.component';
import { Facts } from './facts';

@Component({
  selector: 'kpn-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <p i18n-title="@@route.facts">Facts</p>
    @for (factInfo of filteredFactInfos; track $index) {
      <div class="fact">
        <div>
          <kpn-fact-level [factLevel]="factLevel(factInfo)" class="level" />
          <kpn-fact-name [fact]="factInfo.fact"></kpn-fact-name>

          @if (factInfo.networkRef) {
            <div class="reference">
              <kpn-brackets>
                <a class="text" [routerLink]="'/analysis/network/' + factInfo.networkRef.id">{{
                  factInfo.networkRef.name
                }}</a>
              </kpn-brackets>
            </div>
          }

          @if (factInfo.routeRef) {
            <div class="reference">
              <kpn-brackets>
                <kpn-link-route
                  [routeId]="factInfo.routeRef.id"
                  [routeName]="factInfo.routeRef.name"
                />
              </kpn-brackets>
            </div>
          }

          @if (factInfo.nodeRef) {
            <div class="reference">
              <kpn-brackets>
                <kpn-link-node [nodeId]="factInfo.nodeRef.id" [nodeName]="factInfo.nodeRef.name" />
              </kpn-brackets>
            </div>
          }
        </div>
        <div class="description">
          <kpn-fact-description [factInfo]="factInfo" />
        </div>
      </div>
    } @empty {
      <div i18n="@@facts.none">None</div>
    }
  `,
  styles: `
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
  imports: [
    BracketsComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    RouterLink,
  ],
})
export class FactsComponent {
  factInfos = input.required<FactInfo[]>();

  get filteredFactInfos(): FactInfo[] {
    return this.factInfos().filter((factInfo) => factInfo.fact !== 'RouteBroken');
  }

  factLevel(factInfo: FactInfo): FactLevel {
    return Facts.factLevel(factInfo.fact);
  }
}
