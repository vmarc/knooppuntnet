import { NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { MetaData } from '@api/common/data';
import { MetaDataComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-version-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-thin">
      <ng-container
        *ngIf="isNewVersion()"
        i18n="@@version-change.relation-changed-to"
      >
        Relation changed to v{{ after.version }}.
      </ng-container>
      <span
        *ngIf="!isNewVersion()"
        i18n="@@version-change.relation-unchanged"
        class="kpn-label"
        >Relation unchanged</span
      >
      <kpn-meta-data [metaData]="after" />
    </div>
  `,
  standalone: true,
  imports: [NgIf, MetaDataComponent],
})
export class VersionChangeComponent {
  @Input() before: MetaData;
  @Input() after: MetaData;

  isNewVersion(): boolean {
    if (this.before && this.after) {
      return this.before.version !== this.after.version;
    }
    return false;
  }
}
