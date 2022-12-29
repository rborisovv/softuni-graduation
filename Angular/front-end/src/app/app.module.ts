import {NgModule} from '@angular/core';
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
import {authReducer, authStatusReducer} from "./store/reducer/auth.reducer";

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
    JwtModule.forRoot({
      config: {
        allowedDomains: ['http://localhost:8080/'],
        headerName: 'JWT-TOKEN',
        skipWhenExpired: true
      }
    }),
    StoreModule.forRoot({auth: authReducer, authStatus: authStatusReducer}, {})
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
