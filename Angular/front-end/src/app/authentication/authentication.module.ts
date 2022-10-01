import {Inject, NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {LoginComponent} from './login/login.component';
import {RegisterComponent} from './register/register.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {RouterModule} from "@angular/router";
import {ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../service/user.service";
import {CommonModule as SharedModule} from '../common/common.module';
import {CookieService} from "ngx-cookie-service";
import {JwtModule} from "@auth0/angular-jwt";


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterModule,
    ReactiveFormsModule,
    SharedModule,
    JwtModule.forRoot({
      config: {
        allowedDomains: ['http://localhost:8080/'],
        headerName: 'X-XSRF-TOKEN',
        skipWhenExpired: true
      }
    })
  ],
  providers: [UserService, DatePipe, CookieService],
  exports: [LoginComponent, RegisterComponent]
})
export class AuthenticationModule {
}
