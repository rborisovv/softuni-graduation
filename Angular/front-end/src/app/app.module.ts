import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from "./shared/shared.module";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthenticationModule } from "./authentication/authentication.module";
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from "@angular/common/http";
import { CoreModule } from "./core/core.module";
import { JwtModule } from "@auth0/angular-jwt";
import { XsrfInterceptor } from "./interceptor/xsrf.interceptor";
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { NotifierModule } from "angular-notifier";
import { EffectsModule } from "@ngrx/effects";
import { HeadersDecoratorInterceptor } from "./interceptor/headers.decorator.interceptor";
import { ErrorHandlerInterceptor } from "./interceptor/error.handler.interceptor";
import { authReducer } from "./store/reducer/auth.reducer";
import {
  basketProductsReducer,
  favouriteProductsReducer,
} from "./store/reducer/user.reducer";
import { UrlNormalizerPipe } from './pipes/url.normalizer.pipe';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
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
          distance: 110,
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
        allowedDomains: ['http://localhost:8080'],
        headerName: 'JWT-TOKEN',
        skipWhenExpired: true
      }
    }),
    StoreModule.forRoot({
      auth: authReducer, favouriteProducts: favouriteProductsReducer,
      basketProducts: basketProductsReducer
    }, {}),
    StoreDevtoolsModule.instrument({ maxAge: 25, logOnly: !isDevMode() }),
    EffectsModule.forRoot([]),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: XsrfInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HeadersDecoratorInterceptor,
      multi: true
    }],
  exports: [
    UrlNormalizerPipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}
