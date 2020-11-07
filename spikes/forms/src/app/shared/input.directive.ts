import {Directive, ElementRef} from "@angular/core";
import {DoCheck} from "@angular/core";
import {FormGroupDirective} from "@angular/forms";

@Directive({
  selector: "[our-own-input]"
})
export class InputDirective implements DoCheck {

  private submitted = false;

  constructor(private elementRef: ElementRef,
              private parentFormGroup: FormGroupDirective) {
  }

  ngDoCheck(): void {
    const formGroupSubmitted = this.parentFormGroup.submitted;
    if (formGroupSubmitted !== this.submitted) {
      this.submitted = formGroupSubmitted;
      if (this.submitted) {
        this.elementRef.nativeElement.classList.add("our-own-submitted");
      } else {
        this.elementRef.nativeElement.classList.remove("our-own-submitted");
      }
    }
  }

}
