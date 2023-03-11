import { ChangeDetectionStrategy, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { selectFrisoMode } from '@app/friso/store/friso.selectors';
import { actionFrisoMode } from '@app/friso/store/friso.actions';
import { actionFrisoPageInit } from '@app/friso/store/friso.actions';
import { MatRadioChange } from '@angular/material/radio';

@Component({
  selector: 'kpn-friso-sidebar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-sidebar>
    <div class="mode-selector">
      <mat-radio-group [value]="mode$ | async" (change)="modeChanged($event)">
        <div>
          <mat-radio-button value="rename">
            <span>Rename</span>
            <span class="kpn-brackets">{{ 94 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Node is still in the same place, but has a different number.
          </div>
        </div>
        <div>
          <mat-radio-button value="minor-rename">
            <span>Minor rename</span>
            <span class="kpn-brackets">{{ 33 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Same as rename, but used for minor renames. This is when node
            differs in name by only a few letters.
          </div>
        </div>
        <div>
          <mat-radio-button value="removed">
            <span>Removed</span>
            <span class="kpn-brackets">{{ 4104 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Node is not present in import dataset, but is in OSM.
          </div>
        </div>
        <div>
          <mat-radio-button value="added">
            <span>Added</span>
            <span class="kpn-brackets">{{ 3226 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Node is not present in OSM, but is in the import dataset.
          </div>
        </div>
        <div>
          <mat-radio-button value="no-change">
            <span>No change</span>
            <span class="kpn-brackets">{{ 4611 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Nothing is different between the OSM and import node.
          </div>
        </div>
        <div>
          <mat-radio-button value="moved-short-distance">
            <span>Moved short distance</span>
            <span class="kpn-brackets">{{ 19190 }}</span>
          </mat-radio-button>
          <div class="mode-comment">Node moved a distance of <100m.</div>
        </div>
        <div>
          <mat-radio-button value="moved-long-distance">
            <span>Moved long distance</span>
            <span class="kpn-brackets">{{ 379 }}</span>
          </mat-radio-button>
          <div class="mode-comment">Node moved a distance of 100-1000m.</div>
        </div>
        <div>
          <mat-radio-button value="other">
            <span>Other</span>
            <span class="kpn-brackets">{{ 0 }}</span>
          </mat-radio-button>
          <div class="mode-comment">
            Could not be determined to be in one of the above categories.
          </div>
        </div>
      </mat-radio-group>
    </div>
  </kpn-sidebar>`,
  styles: [
    `
      .mode-selector {
        width: 300px;
        padding: 25px 15px 25px 25px;
      }

      .mode-comment {
        color: grey;
        margin-left: 3em;
        margin-bottom: 1em;
        font-size: 14px;
      }
    `,
  ],
})
export class FrisoSidebarComponent implements OnInit {
  mode$ = this.store.select(selectFrisoMode);

  constructor(private store: Store) {}

  ngOnInit(): void {
    this.store.dispatch(actionFrisoPageInit());
  }

  modeChanged(change: MatRadioChange): void {
    this.store.dispatch(actionFrisoMode({ mode: change.value }));
  }
}