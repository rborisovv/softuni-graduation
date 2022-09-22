import {Component, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faUser} from '@fortawesome/free-regular-svg-icons';
import {faKey} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {IUser} from "../../interface/user-login";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  private subscriptions: Subscription[] = [];

  faFacebook = faFacebookF;
  faTwitter = faTwitter;
  faGoogle = faGoogle;
  faGithub = faGithub;
  faUser = faUser;
  faKey = faKey;

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {

  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  public onLogin(user: IUser) {
    const subscription: Subscription = this.userService.loginUser(user).subscribe({
      next: () => {
        this.router.navigateByUrl('/home');
      }
    });
    this.subscriptions.push(subscription);
  }
}
