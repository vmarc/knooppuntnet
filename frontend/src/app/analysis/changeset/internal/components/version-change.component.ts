import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MetaData } from '@api/common/data/meta-data';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';

@Component({
  selector: 'ui-version-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-thin">
      @if (isNewVersion()) {
        <span i18n="@@version-change.relation-changed-to">
          Relation changed to v{{ after().version }}.
        </span>
      } @else {
        <span i18n="@@version-change.relation-unchanged" class="kpn-label">Relation unchanged</span>
      }
      <ui-meta-data [metaData]="after()" />
    </div>
  `,
  imports: [MetaDataComponent],
})
export class VersionChangeComponent {
  before = input.required<MetaData>();
  after = input.required<MetaData>();

  isNewVersion(): boolean {
    if (this.before() && this.after()) {
      return this.before().version !== this.after().version;
    }
    return false;
  }
}
