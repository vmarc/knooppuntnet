import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChildren } from '@angular/core';
import { inject } from '@angular/core';
import { viewChild } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatAccordion } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { LocationFact } from '@api/common/location';
import { Fact } from '@api/custom';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ExpandCollapseComponent } from '../../../../shared/components/shared/button/expand-collapse.component';
import { RouterService } from '../../../../shared/services/router.service';
import { LocationFactPanelContentsComponent } from './location-fact-panel-contents.component';
import { LocationFactPanelHeaderComponent } from './location-fact-panel-header.component';

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
          <mat-expansion-panel togglePosition="before" (opened)="updateFact(locationFact.fact)">
            <mat-expansion-panel-header>
              <kpn-location-fact-panel-header [locationFact]="locationFact" />
            </mat-expansion-panel-header>
            <ng-template matExpansionPanelContent>
              <kpn-location-fact-panel-contents [locationFact]="locationFact" />
            </ng-template>
          </mat-expansion-panel>
        }
      </mat-accordion>
    }
  `,
  standalone: true,
  imports: [
    IconHappyComponent,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    ExpandCollapseComponent,
    LocationFactPanelHeaderComponent,
    LocationFactPanelContentsComponent,
  ],
})
export class LocationFactsComponent implements AfterViewInit {
  locationFacts = input.required<LocationFact[]>();
  protected readonly accordion = viewChild(MatAccordion);

  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);

  private readonly panels = viewChildren(MatExpansionPanel);
  private readonly panelElementRefs = viewChildren(MatExpansionPanel, { read: ElementRef });
  private readonly expandCollapseActive = signal(false);

  ngAfterViewInit(): void {
    if (this.locationFacts() && this.routerService.fragment()) {
      const fact = this.routerService.fragment();
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

  expandCollapseActiveChanged(active: boolean): void {
    this.expandCollapseActive.set(active);
    if (!active) {
      this.updateFact(null);
    }
  }

  updateFact(fact: Fact): void {
    if (!this.expandCollapseActive()) {
      this.router.navigate([], {
        fragment: fact,
        replaceUrl: true, // do not push a new entry to the browser history
        queryParamsHandling: 'merge', // preserve other query params if there are any
      });
    }
  }
}
