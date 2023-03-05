import {NgModule} from '@angular/core';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {UserService} from "../service/user.service";
import {CookieService} from "ngx-cookie-service";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {AuthHeaderComponent} from './auth-header/auth-header.component';
import {BreadcrumbComponent} from './breadcrumb/breadcrumb.component';
import {CommonModule, KeyValuePipe} from "@angular/common";


@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    AuthHeaderComponent,
    BreadcrumbComponent
  ],
  imports: [
    CommonModule,
    FontAwesomeModule,
    RouterLink,
    RouterLinkActive,
    KeyValuePipe
  ],
    exports: [
        HeaderComponent,
        AuthHeaderComponent,
        BreadcrumbComponent,
        FooterComponent
    ],
  providers: [
    UserService, CookieService
  ]
})
export class SharedModule {
}
