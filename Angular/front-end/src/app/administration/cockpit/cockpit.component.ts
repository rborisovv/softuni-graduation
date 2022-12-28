import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {selectUser} from "../../store/selector/auth.selector";
import {map, tap} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-cockpit',
  templateUrl: './cockpit.component.html',
  styleUrls: ['./cockpit.component.scss']
})
export class CockpitComponent implements OnInit {

  public username: string;

  constructor(private cookieService: CookieService, private jwtService: JwtHelperService) {
  }

  ngOnInit(): void {
    const token = this.cookieService.get('JWT-TOKEN');
    this.username = this.jwtService.decodeToken(token).sub;
  }
}
