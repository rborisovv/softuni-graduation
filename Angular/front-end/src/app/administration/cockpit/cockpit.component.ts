import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {selectUserState} from "../../store/selector/auth.selector";
import {map, Observable} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-cockpit',
  templateUrl: './cockpit.component.html',
  styleUrls: ['./cockpit.component.scss']
})
export class CockpitComponent implements OnInit {

  constructor(private cookieService: CookieService, private jwtService: JwtHelperService,
              private readonly store: Store) {
  }

  public username$: Observable<string>;

  ngOnInit(): void {
    this.username$ = this.store.select(selectUserState)
      .pipe(map(user => user.username));
  }
}
