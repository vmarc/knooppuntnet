import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { LocationFact } from '@api/common/location/location-fact';
import { Fact } from '@api/custom/fact';
import { AppState } from '@app/core/core.state';
import { Store } from '@ngrx/store';
import { actionSharedEdit } from '../../../core/shared/shared.actions';
import { FactInfo } from '../../fact/fact-info';
import { FactLevel } from '../../fact/fact-level';
import { Facts } from '../../fact/facts';

@Component({
  selector: 'kpn-location-facts',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="locationFacts.length === 0" class="kpn-line kpn-spacer-above">
      <span i18n="@@location-facts.none">No facts</span>
      <kpn-icon-happy />
    </div>

    <kpn-items *ngIf="locationFacts.length > 0">
      <kpn-item
        *ngFor="let locationFact of locationFacts; let i = index"
        [index]="i"
      >
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
        <div
          *ngIf="locationFact.elementType === 'route'"
          class="elements kpn-comma-list"
        >
          <kpn-link-route
            *ngFor="let ref of locationFact.refs"
            [routeId]="ref.id"
            [title]="ref.name"
          />
        </div>
        <div
          *ngIf="locationFact.elementType === 'node'"
          class="elements kpn-comma-list"
        >
          <kpn-link-node
            *ngFor="let ref of locationFact.refs"
            [nodeId]="ref.id"
            [nodeName]="ref.name"
          />
        </div>
      </kpn-item>
    </kpn-items>
  `,
  styles: [
    `
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
  ],
})
export class LocationFactsComponent {
  @Input() locationFacts: LocationFact[];

  constructor(private store: Store) {}

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

    this.store.dispatch(actionSharedEdit(editParameters));
  }

  factInfo(locationFact: LocationFact): FactInfo {
    return new FactInfo(locationFact.fact);
  }
}
