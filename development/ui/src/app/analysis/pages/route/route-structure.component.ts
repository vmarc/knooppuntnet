import {Component, Input} from "@angular/core";
import {List} from "immutable";

@Component({
  selector: 'kpn-route-structure',
  template: `
    <table>
      <tbody>
        <tr *ngFor="let structureString of structureStrings">
          <td>
            {{formatted(structureString)}}
          </td>
        </tr>
      </tbody>
    </table>
  `
})
export class RouteStructureComponent {

  @Input() structureStrings: List<string>;

  formatted(structureString: string): string {
    return structureString;
    // TODO
    // return structureString.replaceAll("forward", "<b>forward</b>").replaceAll("backward", "<b>backward</b>").replaceAll("unused", "<b>unused</b>").replaceAll("tentacle", "<b>tentacle</b>").//    replaceAll("broken", """<span style="color:red">broken</span>""").
    // replaceAll("\\+", " + ").replaceAll("-", " - ");
  }
}
