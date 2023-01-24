import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkFact } from '@api/common/network-fact';
import { Store } from '@ngrx/store';
import { AppState } from '../../../core/core.state';
import { actionSharedEdit } from '../../../core/shared/shared.actions';
import { EditParameters } from '../../components/edit/edit-parameters';
import { FactInfo } from '../../fact/fact-info';
import { FactLevel } from '../../fact/fact-level';
import { Facts } from '../../fact/facts';

@Component({
  selector: 'kpn-network-fact-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <span class="kpn-thick">
        <kpn-fact-name [fact]="fact.name"></kpn-fact-name>
      </span>
      <span class="kpn-brackets">{{ factCount() }}</span>
      <kpn-fact-level [factLevel]="factLevel()" class="level"/>
      <a
        rel="nofollow"
        (click)="edit(fact)"
        title="Open in editor (like JOSM)"
        i18n-title="@@edit.link.title"
        i18n="@@edit.link"
        >edit</a
      >
    </div>
    <div class="description">
      <kpn-fact-description [factInfo]="factInfo(fact)"/>
    </div>
  `,
  styles: [
    `
      .description {
        max-width: 60em;
      }
    `,
  ],
})
export class NetworkFactHeaderComponent {
  @Input() fact: NetworkFact;

  constructor(private store: Store) {}

  factLevel(): FactLevel {
    return Facts.factLevels.get(this.fact.name);
  }

  factCount(): number {
    if (this.fact.elements && this.fact.elements.length > 0) {
      return this.fact.elements.length;
    }
    if (this.fact.elementIds && this.fact.elementIds.length > 0) {
      return this.fact.elementIds.length;
    }
    if (this.fact.checks) {
      return this.fact.checks.length;
    }
  }

  edit(networkFact: NetworkFact): void {
    let editParameters: EditParameters = null;

    if (networkFact.elementType === 'node') {
      editParameters = {
        nodeIds: networkFact.elementIds,
      };
    } else if (networkFact.elementType === 'route') {
      editParameters = {
        relationIds: networkFact.elementIds,
        fullRelation: true,
      };
    } else if (networkFact.checks && networkFact.checks.length > 0) {
      editParameters = {
        nodeIds: networkFact.checks.map((check) => check.nodeId),
      };
    }
    this.store.dispatch(actionSharedEdit(editParameters));
  }

  factInfo(networkFact: NetworkFact): FactInfo {
    return new FactInfo(networkFact.name);
  }
}
