import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-node-page-header',
  template: `

    <h1>Node {{nodeName}}</h1>

    <kpn-page-menu>

      <kpn-page-menu-option
        pageName="node"
        [selectedPageName]="pageName"
        [link]="link('node')"
        pageTitle="Details">
      </kpn-page-menu-option>

      <kpn-page-menu-option
        pageName="node-changes"
        [selectedPageName]="pageName"
        [link]="link('node-changes')"
        pageTitle="History">
      </kpn-page-menu-option>

    </kpn-page-menu>

  `
})
export class NodePageHeaderComponent {

  @Input() nodeId;
  @Input() nodeName;
  @Input() pageName;

  link(pageName: string): string {
    return "/analysis/" + pageName + "/" + this.nodeId;
  }

}
