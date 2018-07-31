import {Component, Input} from "@angular/core";
import {Tags} from "../../kpn/shared/data/tags";

@Component({
  selector: 'tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.scss']
})
export class TagsComponent {
  @Input() tags: Tags;
}
