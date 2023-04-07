import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from "./shared/shared.module";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthenticationModule } from "./authentication/authentication.module";
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule, HttpClientXsrfModule } from "@angular/common/http";
import { CoreModule } from "./core/core.module";
import { JwtModule } from "@auth0/angular-jwt";
import { XsrfInterceptor } from "./interceptor/xsrf.interceptor";
import { ActionReducer, MetaReducer, State, StoreModule } from '@ngrx/store';
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
import { localStorageSync } from "ngrx-store-localstorage";
import { discountedTotalReducer } from "./store/reducer/basket.reducer";
import { catchError, Observable, of } from "rxjs";

function localStorageSyncReducer(reducer: ActionReducer<State<any>>): ActionReducer<State<any>> {
  return localStorageSync({
    keys: [
      { auth: ['username', 'email'] },
      { favouriteProducts: ['favouriteProducts'] },
      { basketProducts: ['basketProducts'] },
      { discountedTotal: ['total'] }
    ],
    rehydrate: true
  })(reducer);
}

const metaReducers: Array<MetaReducer> = [localStorageSyncReducer];

function fetchCsrfToken(httpClient: HttpClient): () => Observable<any> {
  return () => httpClient.get('http://localhost:8080/auth/csrf').pipe(catchError(() => of(null)));
}

function provideErrorHandlerInterceptor(interceptor: ErrorHandlerInterceptor): ErrorHandlerInterceptor {
  return interceptor;
}

function provideXsrfInterceptor(interceptor: XsrfInterceptor): XsrfInterceptor {
  return interceptor;
}

function provideHeadersDecoratorInterceptor(interceptor: HeadersDecoratorInterceptor): HeadersDecoratorInterceptor {
  return interceptor;
}

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
      basketProducts: basketProductsReducer, discountedTotal: discountedTotalReducer
    }, { metaReducers: metaReducers }),
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      // logOnly: !isDevMode()
    }),
    EffectsModule.forRoot([]),
  ],
  providers: [
    ErrorHandlerInterceptor,
    XsrfInterceptor,
    HeadersDecoratorInterceptor,
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: provideErrorHandlerInterceptor,
      deps: [ErrorHandlerInterceptor],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: provideXsrfInterceptor,
      deps: [XsrfInterceptor],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useFactory: provideHeadersDecoratorInterceptor,
      deps: [HeadersDecoratorInterceptor],
      multi: true
    },
    {
      provide: APP_INITIALIZER,
      useFactory: fetchCsrfToken,
      deps: [HttpClient],
      multi: true
    }],
  exports: [
    UrlNormalizerPipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}


//TODO: Add fav-icon
