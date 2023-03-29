import { Component, OnInit } from '@angular/core';
import { UserService } from "../../service/user.service";
import { Observable } from "rxjs";
import { User } from "../../model/user";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  users$: Observable<User[]>;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.loadAllUsers();
  }

}
