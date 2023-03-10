import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FrisoNode } from '@app/components/ol/components/friso-node';

@Component({
  selector: 'kpn-friso-node-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
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
})
export class FrisoNodeDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public node: FrisoNode) {}
}
