import {ComponentFactory} from '@angular/core';
import {ComponentFactoryResolver} from '@angular/core';
import {NgModule} from '@angular/core';
import {Feature2Component} from './feature-2.component';

@NgModule({
  declarations: [
    Feature2Component,
  ]
})
export class Feature2Module {

  constructor(private componentFactoryResolver: ComponentFactoryResolver) {
  }

  public resolveComponent(): ComponentFactory<Feature2Component> {
    return this.componentFactoryResolver.resolveComponentFactory(Feature2Component);
  }
}
