import {NgModule, isDevMode} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {SharedModule} from "./shared/shared.module";
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {AuthenticationModule} from "./authentication/authentication.module";
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {CoreModule} from "./core/core.module";
import {JwtModule} from "@auth0/angular-jwt";
import {XsrfInterceptor} from "./interceptor/xsrf-interceptor";
import {StoreModule} from '@ngrx/store';
import {authReducer} from "./store/reducer/auth.reducer";
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {NotifierModule} from "angular-notifier";

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
    NotifierModule.withConfig({
      position: {
        horizontal: {
          position: "right"
        },
        vertical: {
          position: "top",
          distance: 100,
          gap: 10
        }
      },
      behaviour: {
        autoHide: 5000,
        onClick: "hide",
        onMouseover: "pauseAutoHide",
        showDismissButton: true,
        stacking: 4
      }

    }),
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN'
    }),
    JwtModule.forRoot({
      config: {
        allowedDomains: ['http://localhost:8080/'],
        headerName: 'JWT-TOKEN',
        skipWhenExpired: true
      }
    }),
    StoreModule.forRoot({auth: authReducer}, {}),
    StoreDevtoolsModule.instrument({maxAge: 25, logOnly: !isDevMode()})
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
