import { signal } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { Router } from '@angular/router';
import { LocationFact } from '@api/common/location/location-fact';
import { ExpandCollapseComponent } from '@app/shared/components/button/expand-collapse.component';
import { IconHappyComponent } from '@app/shared/components/icon/icon-happy.component';
import { RouterService } from '@app/shared/services/router.service';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { LocationFactPanelContentsComponent } from './location-fact-panel-contents.component';
import { LocationFactPanelHeaderComponent } from './location-fact-panel-header.component';

interface LocationFactPanel {
  readonly locationFact: LocationFact;
  readonly active: boolean;
}

@Component({
  selector: 'ui-location-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @if (locationFacts().length === 0) {
      <div class="kpn-line kpn-spacer-above">
        <span i18n="@@location-facts.none">No facts</span>
        <ui-icon-happy />
      </div>
    } @else {
      <ui-expand-collapse (expandAll)="expandAll()" (collapseAll)="collapseAll()" />
      <nz-collapse>
        @for (panel of panels(); track panel.locationFact.fact) {
          <nz-collapse-panel
            [nzHeader]="header"
            [nzActive]="panel.active"
            (nzActiveChange)="activeChange(panel, $event)"
          >
            <ng-template #header>
              <ui-location-fact-panel-header [locationFact]="panel.locationFact" />
            </ng-template>
            <ui-location-fact-panel-contents [locationFact]="panel.locationFact" />
          </nz-collapse-panel>
        }
      </nz-collapse>
    }
  `,
  imports: [
    ExpandCollapseComponent,
    IconHappyComponent,
    LocationFactPanelContentsComponent,
    LocationFactPanelHeaderComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
  ],
})
export class LocationFactsComponent implements AfterViewInit {
  private readonly routerService = inject(RouterService);
  private readonly router = inject(Router);

  locationFacts = input.required<LocationFact[]>();
  panels = signal<LocationFactPanel[]>([]);

  ngAfterViewInit(): void {
    const selectedFact = this.routerService.fragment();
    const panels = this.locationFacts().map((locationFact) => {
      const active = locationFact.fact === selectedFact;
      return { locationFact, active };
    });
    this.panels.set(panels);
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

  activeChange(panel: LocationFactPanel, active: boolean) {
    const updated = this.panels().map((p) => {
      if (p.locationFact.fact === panel.locationFact.fact) {
        return { ...p, active };
      }
      return p;
    });
    this.panels.set(updated);

    if (active) {
      this.router.navigate([], {
        fragment: panel.locationFact.fact,
        replaceUrl: true, // do not push a new entry to the browser history
        queryParamsHandling: 'merge', // preserve other query params if there are any
      });
    }
  }
}
