import { Location } from '@angular/common';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { inject } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class NodeService {
  private readonly _nodeId = signal<number>(null);
  private readonly _nodeName = signal<string>(null);
  private readonly _changeCount = signal<number>(null);
  private readonly _pageName = signal<string>('details');
  private readonly _nodeNotFound = signal<boolean>(false);

  readonly nodeId = this._nodeId.asReadonly();
  readonly nodeName = this._nodeName.asReadonly();
  readonly changeCount = this._changeCount.asReadonly();
  readonly pageName = this._pageName.asReadonly();
  readonly nodeNotFound = this._nodeNotFound.asReadonly();

  private location = inject(Location);

  onInit(newNodeId: number) {
    const oldNodeId = this.nodeId();
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

  updatePageName(value: string): void {
    this._pageName.set(value);
  }

  updateNodeNotFound(value: boolean): void {
    this._nodeNotFound.set(value);
  }
}
