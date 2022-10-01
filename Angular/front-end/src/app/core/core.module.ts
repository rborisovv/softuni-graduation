import {NgModule} from '@angular/core';
import {HomeComponent} from './home/home.component';
import {CommonModule as SharedModule} from "../common/common.module";
import {CityService} from "../service/city.service";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {XsrfInterceptor} from "../interceptor/xsrf-interceptor";
import {CommonModule} from "@angular/common";

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    SharedModule
  ],
  providers: [CityService, {
    provide: HTTP_INTERCEPTORS,
    useClass: XsrfInterceptor,
    multi: true
  }]
})
export class CoreModule {
}
