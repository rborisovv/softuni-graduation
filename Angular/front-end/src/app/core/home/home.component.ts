import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {HttpClient} from "@angular/common/http";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private readonly store: Store, private http: HttpClient,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.obtainCsrf().subscribe();
  }
}
