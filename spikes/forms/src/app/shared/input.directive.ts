import {Directive, ElementRef} from "@angular/core";
import {OnInit} from "@angular/core";
import {OnDestroy} from "@angular/core";
import {NgControl} from "@angular/forms";
import {BehaviorSubject} from "rxjs";
import {Subscription} from "rxjs";

@Directive({
  selector: "[kpn-input]"
})
export class InputDirective implements OnInit, OnDestroy {

  private element: HTMLInputElement;
  private subscription: Subscription;

  constructor(private elementRef: ElementRef,
              private formControl: NgControl) {
    this.element = this.elementRef.nativeElement;
  }

  ngOnInit(): void {
    const submitted$ = new BehaviorSubject<boolean>(false);
    this.formControl.control["submitted"] = submitted$;
    this.subscription = submitted$.subscribe(submitted => {
      if (submitted) {
        this.element.classList.add("kpn-submitted");
      } else {
        this.element.classList.remove("kpn-submitted");
      }
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
