import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommonModule} from "./common/common.module";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthenticationModule} from "./authentication/authentication.module";
import {HttpClientModule} from "@angular/common/http";
import {CoreModule} from "./core/core.module";
import {NotifierModule} from "angular-notifier";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    CommonModule,
    AuthenticationModule,
    CoreModule,
    HttpClientModule,
    NotifierModule.withConfig(
      {
        position: {
          horizontal: {
            position: 'right',
            distance: 20,
          },
          vertical: {
            position: 'top',
            distance: 70,
            gap: 10,
          },
        },
        theme: 'material',
        behaviour: {
          autoHide: 5000,
          onClick: false,
          onMouseover: 'pauseAutoHide',
          showDismissButton: true,
          stacking: 4,
        },
        animations: {
          enabled: true,
          show: {
            preset: 'slide',
            speed: 300,
            easing: 'ease',
          },
          hide: {
            preset: 'fade',
            speed: 300,
            easing: 'ease',
            offset: 50,
          },
          shift: {
            speed: 300,
            easing: 'ease',
          },
          overlap: 150,
        },
      }
    )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
