import {Component, Input} from "@angular/core";

@Component({
  selector: 'data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.scss']
})
export class DataComponent {

  @Input() title;
  @Input() anchorId;

}
