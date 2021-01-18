import {ComponentFactory} from '@angular/core';
import {ComponentFactoryResolver} from '@angular/core';
import {NgModule} from '@angular/core';
import {Feature1Component} from './feature-1.component';

@NgModule({
  declarations: [
    Feature1Component,
  ]
})
export class Feature1Module {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public resolveComponent(): ComponentFactory<Feature1Component> {
    return this.componentFactoryResolver.resolveComponentFactory(Feature1Component);
  }
}
