import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationFact } from '@api/common/location';
import { Fact } from '@api/custom';
import { FactInfo } from '@app/analysis/fact';
import { FactLevel } from '@app/analysis/fact';
import { Facts } from '@app/analysis/fact';
import { FactDescriptionComponent } from '@app/analysis/fact';
import { FactLevelComponent } from '@app/analysis/fact';
import { FactNameComponent } from '@app/analysis/fact';
import { EditService } from '@app/components/shared';
import { IconHappyComponent } from '@app/components/shared/icon';
import { ItemComponent } from '@app/components/shared/items';
import { ItemsComponent } from '@app/components/shared/items';
import { BracketsComponent } from '@app/components/shared/link';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';

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
      <kpn-items>
        @for (locationFact of locationFacts(); track locationFact; let i = $index) {
          <kpn-item [index]="i">
            <div class="kpn-line">
              <kpn-fact-name [fact]="locationFact.fact" />
              <kpn-brackets>{{ locationFact.refs.length }}</kpn-brackets>
              <kpn-fact-level [factLevel]="factLevel(locationFact.fact)" class="level" />
              <a
                rel="nofollow"
                (click)="edit(locationFact)"
                title="Open in editor (like JOSM)"
                i18n-title="@@edit.link.title"
                i18n="@@edit.link"
                >edit</a
              >
            </div>
            <div class="description">
              <kpn-fact-description [factInfo]="factInfo(locationFact)" />
            </div>
            @if (locationFact.elementType === 'route') {
              <div class="elements kpn-comma-list">
                @for (ref of locationFact.refs; track ref) {
                  <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
                }
              </div>
            }
            @if (locationFact.elementType === 'node') {
              <div class="elements kpn-comma-list">
                @for (ref of locationFact.refs; track ref) {
                  <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
                }
              </div>
            }
          </kpn-item>
        }
      </kpn-items>
    }
  `,
  styles: `
    .description {
      max-width: 60em;
      border-left: 1px solid lightgray;
      padding-left: 20px;
    }

    .elements {
      max-width: 60em;
      padding-right: 20px;
    }
  `,
  standalone: true,
  imports: [
    BracketsComponent,
    FactDescriptionComponent,
    FactLevelComponent,
    FactNameComponent,
    IconHappyComponent,
    ItemComponent,
    ItemsComponent,
    LinkNodeComponent,
    LinkRouteComponent,
  ],
})
export class LocationFactsComponent {
  locationFacts = input<LocationFact[] | undefined>();

  private readonly editService = inject(EditService);

  factLevel(fact: Fact): FactLevel {
    return Facts.factLevels.get(fact);
  }

  edit(locationFact: LocationFact): void {
    let editParameters = null;

    if (locationFact.elementType === 'node') {
      editParameters = {
        nodeIds: locationFact.refs.map((ref) => ref.id),
      };
    }

    if (locationFact.elementType === 'route') {
      editParameters = {
        relationIds: locationFact.refs.map((ref) => ref.id),
        fullRelation: true,
      };
    }

    this.editService.edit(editParameters);
  }

  factInfo(locationFact: LocationFact): FactInfo {
    return new FactInfo(locationFact.fact);
  }
}
