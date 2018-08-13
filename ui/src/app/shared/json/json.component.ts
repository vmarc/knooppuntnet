import {Component, Input, OnInit} from "@angular/core";

@Component({
  selector: 'json',
  templateUrl: './json.component.html',
  styleUrls: ['./json.component.scss']
})
export class JsonComponent implements OnInit {

  @Input() object: any;
  contents: string = "";
  visible = false;

  ngOnInit() {
    this.contents = JSON.stringify(this.object, null, 2);
  }

  toggleVisible() {
    this.visible = !this.visible;
  }
}
