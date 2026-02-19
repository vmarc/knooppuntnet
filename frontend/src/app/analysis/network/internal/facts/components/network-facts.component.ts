import { effect } from '@angular/core';
import { input } from '@angular/core';
import { signal } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChildren } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NetworkFact } from '@api/common/network-fact';
import { NetworkFactsPage } from '@api/common/network/network-facts-page';
import { ApiResponse } from '@api/custom/api-response';
import { NetworkFactContentsComponent } from '@app/analysis/network/internal/facts/components/network-fact-contents.component';
import { ExpandCollapseComponent } from '@app/shared/components/button/expand-collapse.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { SituationOnComponent } from '@app/shared/components/timestamp/situation-on.component';
import { RouterService } from '@app/shared/services/router.service';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { NetworkFactHeaderComponent } from './network-fact-header.component';

interface NetworkFactPanel {
  readonly networkFact: NetworkFact;
  readonly active: boolean;
}

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
        <ui-expand-collapse (expandAll)="expandAll()" (collapseAll)="collapseAll()" />
        <nz-collapse>
          @for (panel of panels(); track panel.networkFact.fact) {
            <nz-collapse-panel
              [nzHeader]="header"
              [nzActive]="panel.active"
              (nzActiveChange)="activeChange(panel, $event)"
            >
              <ng-template #header>
                <div class="kpn-align-center">
                  <ui-network-fact-header [networkFact]="panel.networkFact" />
                </div>
              </ng-template>
              <ui-network-fact-contents
                [routeType]="page.summary.routeType"
                [networkFact]="panel.networkFact"
              />
            </nz-collapse-panel>
          }
        </nz-collapse>
      }
    }
  `,
  imports: [
    ExpandCollapseComponent,
    IconHappyComponent,
    NetworkFactContentsComponent,
    NetworkFactHeaderComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    SituationOnComponent,
  ],
})
export class NetworkFactsComponent {
  readonly apiResponse = input.required<ApiResponse<NetworkFactsPage>>();

  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);

  protected readonly panels = signal<NetworkFactPanel[]>([]);

  private readonly panelElementRefs = viewChildren(NetworkFactHeaderComponent, {
    read: ElementRef,
  });

  constructor() {
    effect(() => {
      const networkFacts: ReadonlyArray<NetworkFact> = this.apiResponse().result.facts;
      if (networkFacts.length > 0) {
        const fact = this.routerService.fragment();
        const pp = networkFacts.map((networkFact) => {
          const active = networkFact.fact === fact;
          return { networkFact, active };
        });
        this.panels.set(pp);
      }
    });
    effect(() => {
      const panelsElementRefs = this.panelElementRefs();
      if (panelsElementRefs.length > 0) {
        const networkFacts: ReadonlyArray<NetworkFact> = this.apiResponse().result.facts;
        const fact = this.routerService.fragment();
        const panelIndex = networkFacts.findIndex((networkFact) => networkFact.fact === fact);
        if (panelIndex >= 0 && panelIndex < panelsElementRefs.length) {
          const elementRef = panelsElementRefs.at(panelIndex);
          elementRef.nativeElement.scrollIntoView({
            behavior: 'smooth',
            block: 'start',
          });
        }
      }
    });
  }

  expandAll(): void {
    this.activeAll(true);
  }

  collapseAll(): void {
    this.activeAll(false);
  }

  private activeAll(active: boolean): void {
    const updatedPanels = this.panels().map((p) => {
      return { ...p, active };
    });
    this.panels.set(updatedPanels);
  }

  activeChange(panel: NetworkFactPanel, active: boolean) {
    const updated = this.panels().map((p) => {
      if (p.networkFact.fact === panel.networkFact.fact) {
        return { ...p, active };
      }
      return p;
    });
    this.panels.set(updated);

    if (active) {
      this.router.navigate([], {
        fragment: panel.networkFact.fact,
        replaceUrl: true, // do not push a new entry to the browser history
        queryParamsHandling: 'merge', // preserve other query params if there are any
      });
    }
  }
}
