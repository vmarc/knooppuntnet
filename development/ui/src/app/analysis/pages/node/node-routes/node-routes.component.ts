import {Component, Input} from "@angular/core";
import {Reference} from "../../../../kpn/shared/common/reference";

@Component({
  selector: 'node-routes',
  templateUrl: './node-routes.component.html',
  styleUrls: ['./node-routes.component.scss']
})
export class NodeRoutesComponent {
  @Input() routes: Array<Reference> = [];
}
