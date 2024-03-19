import { Location } from '@angular/common';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';
import { RouterService } from '../../shared/services/router.service';

@Injectable({
  providedIn: 'root',
})
export class NodeService {
  private readonly _nodeId = signal<string>(null);
  private readonly _nodeName = signal<string>(null);
  private readonly _changeCount = signal<number>(null);

  readonly nodeId = this._nodeId.asReadonly();
  readonly nodeName = this._nodeName.asReadonly();
  readonly changeCount = this._changeCount.asReadonly();

  private location = inject(Location);

  initPage(routerService: RouterService): void {
    const oldNodeId = this.nodeId();
    const newNodeId = routerService.param('nodeId');
    if (!oldNodeId || oldNodeId !== newNodeId) {
      this._nodeId.set(newNodeId);
      this._changeCount.set(null);
      let newNodeName: string = undefined;
      const state = this.location.getState();
      if (state) {
        newNodeName = state['nodeName'];
      }
      this._nodeName.set(newNodeName);
    }
  }

  updateNode(nodeName: string, changeCount: number): void {
    this._nodeName.set(nodeName);
    this._changeCount.set(changeCount);
  }
}
