import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'kpn-fact-route-unexpected-relation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <markdown>
      <ng-container i18n="@@fact.description.route-unexpected-relation">
        The route relation contains one or more unexpected relation members. In route relations we
        expect only members of type *"way"* and *"node"*:
      </ng-container>
      <span class="kpn-sentence">
        <span class="kpn-comma-list">
          @for (relationId of factInfo.unexpectedRelationIds; track $index) {
            <kpn-osm-link-relation [relationId]="relationId" [title]="relationId.toString()" />
          }
        </span>
      </span>
    </markdown>
  `,
  standalone: true,
  imports: [MarkdownModule, OsmLinkRelationComponent],
})
export class FactRouteUnexpectedRelationComponent {
  @Input() factInfo;
}
