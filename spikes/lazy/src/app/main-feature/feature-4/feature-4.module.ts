import {ComponentFactory} from '@angular/core';
import {ComponentFactoryResolver} from '@angular/core';
import {NgModule} from '@angular/core';
import {Feature4Component} from './feature-4.component';

@NgModule({
  declarations: [
    Feature4Component,
  ]
})
export class Feature4Module {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public resolveComponent(): ComponentFactory<Feature4Component> {
    return this.componentFactoryResolver.resolveComponentFactory(Feature4Component);
  }

}
