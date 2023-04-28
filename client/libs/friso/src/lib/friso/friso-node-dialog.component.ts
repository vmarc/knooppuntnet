import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from '@app/components/shared/dialog';
import { FrisoNode } from './friso-node';

@Component({
  selector: 'kpn-friso-node-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <kpn-dialog>
      <div mat-dialog-title class="title">
        <div class="title-text">Routedatabank node</div>
      </div>
      <div mat-dialog-content>
        <p>name: {{ node.name }}</p>
        <p>distance to closest node: {{ node.distanceClosestNode }}m</p>
      </div>
    </kpn-dialog>
  `,
  standalone: true,
  imports: [DialogComponent, MatDialogModule],
})
export class FrisoNodeDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public node: FrisoNode) {}
}
