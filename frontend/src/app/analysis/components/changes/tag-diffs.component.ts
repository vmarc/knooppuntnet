import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { TagDiffs } from '@api/common/diff';
import { PageWidthService } from '@app/components/shared';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { TagDiffsTableComponent } from './tag-diffs-table.component';
import { TagDiffsTextComponent } from './tag-diffs-text.component';

@Component({
  selector: 'kpn-tag-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="small$ | async; then small; else large"></div>
    <ng-template #small>
      <kpn-tag-diffs-text
        *ngIf="small$ | async; else large"
        [tagDiffs]="tagDiffs"
      />
    </ng-template>
    <ng-template #large>
      <div class="kpn-label" i18n="@@tag-diffs.title">Tag changes</div>
      <kpn-tag-diffs-table #large [tagDiffs]="tagDiffs" />
    </ng-template>
  `,
  standalone: true,
  imports: [NgIf, TagDiffsTextComponent, TagDiffsTableComponent, AsyncPipe],
})
export class TagDiffsComponent {
  @Input() tagDiffs: TagDiffs;

  small$: Observable<boolean>;

  constructor(private pageWidthService: PageWidthService) {
    this.small$ = pageWidthService.current$.pipe(map(() => this.small()));
  }

  private small(): boolean {
    return (
      this.pageWidthService.isSmall() ||
      this.pageWidthService.isVerySmall() ||
      this.pageWidthService.isVeryVerySmall()
    );
  }
}
