import { input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { signal } from '@angular/core';
import { viewChild } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChildren } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatAccordion } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { NetworkFact } from '@api/common/network-fact';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { Fact } from '@api/common/fact';
import { FactInfo } from '@app/analysis/fact/components/fact-info';
import { FactDescriptionComponent } from '@app/analysis/fact/components/fact-description.component';
import { OldExpandCollapseComponent } from '@app/shared/components/button/old-expand-collapse.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { ActionButtonNodesComponent } from '../../../../components/action/action-button-nodes.component';
import { ActionButtonRelationsComponent } from '../../../../components/action/action-button-relations.component';
import { ActionButtonRoutesComponent } from '../../../../components/action/action-button-routes.component';
import { ActionButtonWaysComponent } from '../../../../components/action/action-button-ways.component';
import { NetworkFactChecksComponent } from './network-fact-checks.component';
import { NetworkFactHeaderComponent } from './network-fact-header.component';
import { NetworkFactNodeIdsComponent } from './network-fact-node-ids.component';
import { NetworkFactNodesComponent } from './network-fact-nodes.component';
import { NetworkFactRelationIdsComponent } from './network-fact-relation-ids.component';
import { NetworkFactRoutesComponent } from './network-fact-routes.component';
import { NetworkFactWayIdsComponent } from './network-fact-way-ids.component';

@Component({
  selector: 'ui-network-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (apiResponse().result; as page) {
      <ui-situation-on [timestamp]="apiResponse().situationOn" />
      @if (page.facts.length === 0) {
        <p class="kpn-line">
          <span i18n="@@network-facts.no-facts">No facts</span>
          <ui-icon-happy />
        </p>
      } @else {
        <ui-old-expand-collapse
          [accordion]="accordion()"
          (active)="expandCollapseActiveChanged($event)"
        />
        <mat-accordion multi>
          @for (fact of page.facts; track fact.fact) {
            <mat-expansion-panel togglePosition="before" (opened)="updateFactFragment(fact.fact)">
              <mat-expansion-panel-header>
                <div class="kpn-align-center">
                  @if (fact.elements && fact.elementType === 'node') {
                    <ui-action-button-nodes [nodeIds]="elementIds(fact)" />
                  } @else if (fact.elements && fact.elementType === 'route') {
                    <ui-action-button-routes [relationIds]="elementIds(fact)" />
                  } @else if (fact.checks && fact.checks.length > 0) {
                    <ui-action-button-nodes [nodeIds]="checkIds(fact)" />
                  } @else if (fact.elementIds) {
                    @switch (fact.elementType) {
                      @case ('node') {
                        <ui-action-button-nodes [nodeIds]="fact.elementIds" />
                      }
                      @case ('way') {
                        <ui-action-button-ways [wayIds]="fact.elementIds" />
                      }
                      @case ('relation') {
                        <ui-action-button-relations [relationIds]="fact.elementIds" />
                      }
                      @case ('route') {
                        <ui-action-button-routes [relationIds]="fact.elementIds" />
                      }
                    }
                  }
                  <ui-network-fact-header [fact]="fact" />
                </div>
              </mat-expansion-panel-header>

              <ng-template matExpansionPanelContent>
                <div class="description">
                  <ui-fact-description [factInfo]="factInfo(fact)" />
                </div>
                <ui-divider />
                <div class="sideline">
                  @if (fact.elements) {
                    @if (fact.elementType === 'route') {
                      <ui-network-fact-routes
                        [routes]="fact.elements"
                        [routeType]="page.summary.routeType"
                      />
                    } @else if (fact.elementType === 'node') {
                      <ui-network-fact-nodes [nodes]="fact.elements" />
                    }
                  } @else if (fact.elementIds) {
                    @switch (fact.elementType) {
                      @case ('node') {
                        <ui-network-fact-node-ids [nodeIds]="fact.elementIds" />
                      }
                      @case ('way') {
                        <ui-network-fact-way-ids [elementIds]="fact.elementIds" />
                      }
                      @case ('relation') {
                        <ui-network-fact-relation-ids [elementIds]="fact.elementIds" />
                      }
                    }
                  }

                  @if (fact.checks && fact.checks.length > 0) {
                    <ui-network-fact-checks [checks]="fact.checks" />
                  }
                </div>
              </ng-template>
            </mat-expansion-panel>
          }
        </mat-accordion>
      }
    }
  `,
  styles: `
    .description {
      max-width: 60em;
    }

    .sideline {
      margin-top: 1em;
      margin-bottom: 1em;
      margin-left: 0.8em;
      padding-left: 0.8em;
      border-left: 1px solid lightgray;
    }
  `,
  imports: [
    ActionButtonNodesComponent,
    ActionButtonRelationsComponent,
    ActionButtonRoutesComponent,
    ActionButtonWaysComponent,
    DividerComponent,
    FactDescriptionComponent,
    IconHappyComponent,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    NetworkFactChecksComponent,
    NetworkFactHeaderComponent,
    NetworkFactNodeIdsComponent,
    NetworkFactNodesComponent,
    NetworkFactRelationIdsComponent,
    NetworkFactRoutesComponent,
    NetworkFactWayIdsComponent,
    SituationOnComponent,
    OldExpandCollapseComponent,
  ],
})
export class NetworkFactsComponent implements AfterViewInit {
  apiResponse = input.required<ApiResponse<NetworkFactsPage>>();

  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);
  private readonly panels = viewChildren(MatExpansionPanel);
  private readonly panelElementRefs = viewChildren(MatExpansionPanel, { read: ElementRef });

  protected readonly accordion = viewChild(MatAccordion);

  private readonly expandCollapseActive = signal(false);

  ngAfterViewInit(): void {
    if (this.apiResponse() && this.routerService.fragment()) {
      const fact = this.routerService.fragment();
      const networkFacts: NetworkFact[] = this.apiResponse().result.facts;
      const panelIndex = networkFacts.findIndex((networkFact) => networkFact.fact === fact);

      if (panelIndex >= 0 && panelIndex < this.panels().length) {
        const panel = this.panels().at(panelIndex);
        panel.open();
        this.panelElementRefs().at(panelIndex).nativeElement.scrollIntoView({
          behavior: 'smooth',
          block: 'center',
        });
      }
    }
  }

  expandCollapseActiveChanged(active: boolean): void {
    this.expandCollapseActive.set(active);
    if (!active) {
      this.updateFactFragment(null);
    }
  }

  factInfo(networkFact: NetworkFact): FactInfo {
    return new FactInfo(networkFact.fact);
  }

  elementIds(networkFact: NetworkFact): number[] {
    return networkFact.elements.map((element) => element.id);
  }

  checkIds(networkFact: NetworkFact): number[] {
    return networkFact.checks.map((check) => check.nodeId);
  }

  updateFactFragment(fact: Fact): void {
    if (!this.expandCollapseActive()) {
      this.router.navigate([], {
        fragment: fact,
        replaceUrl: true, // do not push a new entry to the browser history
        queryParamsHandling: 'merge', // preserve other query params if there are any
      });
    }
  }
}
