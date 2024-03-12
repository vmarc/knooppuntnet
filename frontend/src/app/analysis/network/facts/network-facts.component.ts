import { input } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { signal } from '@angular/core';
import { viewChild } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChildren } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatAccordion } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { NetworkFact } from '@api/common';
import { NetworkFactsPage } from '@api/common/network';
import { ApiResponse } from '@api/custom';
import { Fact } from '@api/custom';
import { FactInfo } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { IconHappyComponent } from '@app/components/shared/icon';
import { SituationOnComponent } from '@app/components/shared/timestamp';
import { selectFragment } from '@app/core';
import { Store } from '@ngrx/store';
import { ExpandCollapseComponent } from '../../../shared/components/shared/button/expand-collapse.component';
import { ActionButtonNodesComponent } from '../../components/action/action-button-nodes.component';
import { ActionButtonRelationsComponent } from '../../components/action/action-button-relations.component';
import { ActionButtonRoutesComponent } from '../../components/action/action-button-routes.component';
import { ActionButtonWaysComponent } from '../../components/action/action-button-ways.component';
import { NetworkFactChecksComponent } from './network-fact-checks.component';
import { NetworkFactHeaderComponent } from './network-fact-header.component';
import { NetworkFactNodeIdsComponent } from './network-fact-node-ids.component';
import { NetworkFactNodesComponent } from './network-fact-nodes.component';
import { NetworkFactRelationIdsComponent } from './network-fact-relation-ids.component';
import { NetworkFactRoutesComponent } from './network-fact-routes.component';
import { NetworkFactWayIdsComponent } from './network-fact-way-ids.component';

@Component({
  selector: 'kpn-network-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (apiResponse().result; as page) {
      <kpn-situation-on [timestamp]="apiResponse().situationOn" />
      @if (page.facts.length === 0) {
        <p class="kpn-line">
          <span i18n="@@network-facts.no-facts">No facts</span>
          <kpn-icon-happy />
        </p>
      } @else {
        <kpn-expand-collapse
          [accordion]="accordion()"
          (active)="expandCollapseActiveChanged($event)"
        />
        <mat-accordion multi>
          @for (fact of page.facts; track fact.name) {
            <mat-expansion-panel togglePosition="before" (opened)="updateFactFragment(fact.name)">
              <mat-expansion-panel-header>
                <div class="kpn-align-center">
                  @if (fact.elements && fact.elementType === 'node') {
                    <kpn-action-button-nodes [nodeIds]="elementIds(fact)" />
                  } @else if (fact.elements && fact.elementType === 'route') {
                    <kpn-action-button-routes [relationIds]="elementIds(fact)" />
                  } @else if (fact.checks && fact.checks.length > 0) {
                    <kpn-action-button-nodes [nodeIds]="checkIds(fact)" />
                  } @else if (fact.elementIds) {
                    @switch (fact.elementType) {
                      @case ('node') {
                        <kpn-action-button-nodes [nodeIds]="fact.elementIds" />
                      }
                      @case ('way') {
                        <kpn-action-button-ways [wayIds]="fact.elementIds" />
                      }
                      @case ('relation') {
                        <kpn-action-button-relations [relationIds]="fact.elementIds" />
                      }
                      @case ('route') {
                        <kpn-action-button-routes [relationIds]="fact.elementIds" />
                      }
                    }
                  }
                  <kpn-network-fact-header [fact]="fact" />
                </div>
              </mat-expansion-panel-header>

              <ng-template matExpansionPanelContent>
                <div class="description">
                  <kpn-fact-description [factInfo]="factInfo(fact)" />
                </div>
                <div class="kpn-small-spacer-above kpn-small-spacer-below">
                  <mat-divider />
                </div>

                <div class="sideline">
                  @if (fact.elements) {
                    @if (fact.elementType === 'route') {
                      <kpn-network-fact-routes
                        [routes]="fact.elements"
                        [networkType]="page.summary.networkType"
                      />
                    } @else if (fact.elementType === 'node') {
                      <kpn-network-fact-nodes [nodes]="fact.elements" />
                    }
                  } @else if (fact.elementIds) {
                    @switch (fact.elementType) {
                      @case ('node') {
                        <kpn-network-fact-node-ids [nodeIds]="fact.elementIds" />
                      }
                      @case ('way') {
                        <kpn-network-fact-way-ids [elementIds]="fact.elementIds" />
                      }
                      @case ('relation') {
                        <kpn-network-fact-relation-ids [elementIds]="fact.elementIds" />
                      }
                    }
                  }

                  @if (fact.checks && fact.checks.length > 0) {
                    <kpn-network-fact-checks [checks]="fact.checks" />
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
  standalone: true,
  imports: [
    IconHappyComponent,
    SituationOnComponent,
    FactDescriptionComponent,
    MatAccordion,
    MatDivider,
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
    ActionButtonNodesComponent,
    ActionButtonWaysComponent,
    ActionButtonRelationsComponent,
    ActionButtonRoutesComponent,
    ExpandCollapseComponent,
  ],
})
export class NetworkFactsComponent implements AfterViewInit {
  apiResponse = input.required<ApiResponse<NetworkFactsPage>>();

  private readonly store = inject(Store);
  private readonly router = inject(Router);
  private readonly fragment = this.store.selectSignal(selectFragment);
  private readonly panels = viewChildren(MatExpansionPanel);
  private readonly panelElementRefs = viewChildren(MatExpansionPanel, { read: ElementRef });

  accordion = viewChild(MatAccordion);

  private readonly expandCollapseActive = signal(false);

  ngAfterViewInit(): void {
    console.log('ngAfterViewInit()');
    if (this.apiResponse() && this.fragment()) {
      const fact = this.fragment();
      const networkFacts: NetworkFact[] = this.apiResponse().result.facts;
      const panelIndex = networkFacts.findIndex((networkFact) => networkFact.name === fact);

      console.log('fact=' + fact);
      console.log('panelIndex=' + panelIndex);

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
    return new FactInfo(networkFact.name);
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
