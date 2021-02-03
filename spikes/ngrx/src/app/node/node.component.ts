import {Component} from '@angular/core';
import {AppService} from '../app.service';

@Component({
  selector: 'app-node',
  template: `
    <h1>Node</h1>

    <div *ngIf="response$ | async as response">

      <p>
        name = {{response.result.nodeInfo.name}}
      </p>

      <p *ngFor="let ref of response.result.references.routeReferences">
        {{ref.networkType}} {{ref.routeId}} {{ref.routeName}}
      </p>

      <pre>
{{json(response)}}
      </pre>
    </div>
  `
})
export class NodeComponent {

  readonly response$ = this.appService.nodeDetails('1');

  constructor(private appService: AppService) {
  }

  json(x: any): string {
    return JSON.stringify(x, null, 2);
  }
}
