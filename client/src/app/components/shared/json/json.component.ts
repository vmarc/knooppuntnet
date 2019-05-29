import {Component, Input, OnInit} from "@angular/core";

@Component({
  selector: "json",
  template: `
    <br/>
    <br/>
    <button (click)="toggleVisible()">JSON</button>
    <div *ngIf="visible">
      <br/>
<pre>
{{contents}}
</pre>
    </div>
    <br/>
    <br/>
  `
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
