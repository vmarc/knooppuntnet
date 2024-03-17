import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChildren } from '@angular/core';
import { inject } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatDivider } from '@angular/material/divider';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatAccordion } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { LocationFact } from '@api/common/location';
import { Fact } from '@api/custom';
import { FactInfo } from '@app/analysis/fact';
import { FactLevel } from '@app/analysis/fact';
import { Facts } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactLevelComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { IconRouteComponent } from '@app/components/shared/icon';
import { IconNodeComponent } from '@app/components/shared/icon';
import { IconHappyComponent } from '@app/components/shared/icon';
import { BracketsComponent } from '@app/components/shared/link';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { selectFragment } from '@app/core';
import { Store } from '@ngrx/store';
import { ExpandCollapseComponent } from '../../../../shared/components/shared/button/expand-collapse.component';
import { ActionButtonLocationFactNodesComponent } from '../../../components/action/action-button-location-fact-nodes.component';
import { ActionButtonLocationFactRoutesComponent } from '../../../components/action/action-button-location-fact-routes.component';
import { ActionButtonNodeComponent } from '../../../components/action/action-button-node.component';
import { ActionButtonRouteComponent } from '../../../components/action/action-button-route.component';

@Component({
  selector: 'kpn-location-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationFacts().length === 0) {
      <div class="kpn-line kpn-spacer-above">
        <span i18n="@@location-facts.none">No facts</span>
        <kpn-icon-happy />
      </div>
    } @else {
      <kpn-expand-collapse
        [accordion]="accordion()"
        (active)="expandCollapseActiveChanged($event)"
      />
      <mat-accordion multi>
        @for (locationFact of locationFacts(); track locationFact.fact) {
          <mat-expansion-panel
            togglePosition="before"
            (opened)="updateFactFragment(locationFact.fact)"
          >
            <mat-expansion-panel-header>
              <div class="kpn-line">
                @if (locationFact.elementType === 'node') {
                  <kpn-action-button-location-fact-nodes [locationFact]="locationFact" />
                } @else if (locationFact.elementType === 'route') {
                  <kpn-action-button-location-fact-routes [locationFact]="locationFact" />
                }
                <kpn-fact-name [fact]="locationFact.fact" />
                <kpn-brackets>{{ locationFact.refs.length }}</kpn-brackets>
                <kpn-fact-level [factLevel]="factLevel(locationFact.fact)" class="level" />
              </div>
            </mat-expansion-panel-header>

            <ng-template matExpansionPanelContent>
              <div class="description">
                <kpn-fact-description [factInfo]="factInfo(locationFact)" />
              </div>
              <div class="kpn-small-spacer-above kpn-small-spacer-below">
                <mat-divider />
              </div>
              <div class="sideline">
                @if (locationFact.elementType === 'route') {
                  @for (ref of locationFact.refs; track ref) {
                    <div class="kpn-align-center">
                      <kpn-icon-route />
                      <kpn-action-button-route [relationId]="ref.id" />
                      <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
                    </div>
                  }
                }
                @if (locationFact.elementType === 'node') {
                  @for (ref of locationFact.refs; track ref) {
                    <div class="kpn-align-center">
                      <kpn-icon-node />
                      <kpn-action-button-node [nodeId]="ref.id" />
                      <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                    </div>
                  }
                }
              </div>
            </ng-template>
          </mat-expansion-panel>
        }
      </mat-accordion>
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
    ActionButtonLocationFactNodesComponent,
    ActionButtonLocationFactRoutesComponent,
    ActionButtonNodeComponent,
    ActionButtonRouteComponent,
    BracketsComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    IconHappyComponent,
    IconNodeComponent,
    IconRouteComponent,
    LinkNodeComponent,
    LinkRouteComponent,
    MatAccordion,
    MatDivider,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    ExpandCollapseComponent,
  ],
})
export class LocationFactsComponent implements AfterViewInit {
  locationFacts = input.required<LocationFact[]>();
  accordion = viewChild(MatAccordion);

  private readonly store = inject(Store);
  private readonly router = inject(Router);

  private readonly fragment = this.store.selectSignal(selectFragment);
  private readonly panels = viewChildren(MatExpansionPanel);
  private readonly panelElementRefs = viewChildren(MatExpansionPanel, { read: ElementRef });

  private readonly expandCollapseActive = signal(false);

  ngAfterViewInit(): void {
    if (this.locationFacts() && this.fragment()) {
      const fact = this.fragment();
      const facts: LocationFact[] = this.locationFacts();
      const panelIndex = facts.findIndex((locationFact) => locationFact.fact === fact);
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

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevel(fact);
  }

  factInfo(locationFact: LocationFact): FactInfo {
    return new FactInfo(locationFact.fact);
  }

  expandCollapseActiveChanged(active: boolean): void {
    this.expandCollapseActive.set(active);
    if (!active) {
      this.updateFactFragment(null);
    }
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
