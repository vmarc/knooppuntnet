import {ComponentFactory} from '@angular/core';
import {ComponentFactoryResolver} from '@angular/core';
import {NgModule} from '@angular/core';
import {Feature3Component} from './feature-3.component';

@NgModule({
  declarations: [
    Feature3Component,
  ]
})
export class Feature3Module {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public resolveComponent(): ComponentFactory<Feature3Component> {
    return this.componentFactoryResolver.resolveComponentFactory(Feature3Component);
  }
}
