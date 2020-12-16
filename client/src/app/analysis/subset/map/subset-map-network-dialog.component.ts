import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {SubsetMapNetwork} from '@api/common/subset/subset-map-network';

@Component({
  selector: 'kpn-subset-map-network-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="dialog">
      <div mat-dialog-title>
        {{network.name}}
      </div>
      <div mat-dialog-content>
        <div class="kpn-km">{{network.km}}</div>
        <div>
          <span>{{network.nodeCount}}</span>&nbsp;
          <span i18n="@@subset-map.dialog.nodeCount">nodes</span>
        </div>
        <div>
          <span>{{network.routeCount}}</span>&nbsp;
          <span i18n="@@subset-map.dialog.routeCount">routes</span>
        </div>
        <div class="link">
          <a
            [routerLink]="'/analysis/network/' + network.id"
            (click)="closeDialog()"
            i18n="@@subset-map.dialog.show-network-details">
            Show network details
          </a>
        </div>
      </div>
      <div mat-dialog-actions>
        <button mat-stroked-button (click)="closeDialog()" i18n="@@subset-map.dialog.close">Close</button>
      </div>
    </div>
  `,
  styles: [`

    .dialog {
      min-width: 20em;
    }

    .link {
      padding-top: 2em;
    }

  `]
})
export class SubsetMapNetworkDialogComponent {

  constructor(private dialogRef: MatDialogRef<SubsetMapNetworkDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public network: SubsetMapNetwork) {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

}
