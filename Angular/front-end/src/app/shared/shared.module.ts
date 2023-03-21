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
import {UrlNormalizerPipe} from "../pipes/url.normalizer.pipe";
import { MatPaginatorModule } from "@angular/material/paginator";
import { HamburgerComponent } from './hamburger/hamburger.component';


@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    AuthHeaderComponent,
    BreadcrumbComponent,
    UrlNormalizerPipe,
    HamburgerComponent
  ],
    imports: [
        CommonModule,
        FontAwesomeModule,
        RouterLink,
        RouterLinkActive,
        KeyValuePipe,
        MatPaginatorModule
    ],
  exports: [
    HeaderComponent,
    AuthHeaderComponent,
    BreadcrumbComponent,
    FooterComponent,
    UrlNormalizerPipe,
    HamburgerComponent
  ],
  providers: [
    UserService, CookieService
  ]
})
export class SharedModule {
}
