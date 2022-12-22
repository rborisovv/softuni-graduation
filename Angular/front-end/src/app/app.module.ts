import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SharedModule} from "./shared/shared.module";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthenticationModule} from "./authentication/authentication.module";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {CoreModule} from "./core/core.module";
import {NotifierModule} from "angular-notifier";
import {JwtModule} from "@auth0/angular-jwt";
import {XsrfInterceptor} from "./interceptor/xsrf-interceptor";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    SharedModule,
    AuthenticationModule,
    CoreModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN'
    }),
    NotifierModule.withConfig(
      {
        position: {
          horizontal: {
            position: 'right',
            distance: 20,
          },
          vertical: {
            position: 'top',
            distance: 90,
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
    ),
    JwtModule.forRoot({
      config: {
        allowedDomains: ['http://localhost:8080/'],
        headerName: 'X-XSRF-TOKEN',
        skipWhenExpired: true
      }
    })
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: XsrfInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
