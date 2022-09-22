import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {CommonModule} from "./common/common.module";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {AuthenticationModule} from "./authentication/authentication.module";
import {HttpClientModule} from "@angular/common/http";
import {CoreModule} from "./core/core.module";

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
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
