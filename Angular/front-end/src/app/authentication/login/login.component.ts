import {Component, OnDestroy, OnInit} from '@angular/core';
import {faFacebookF, faGithub, faGoogle, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {faUser} from '@fortawesome/free-regular-svg-icons';
import {faKey} from '@fortawesome/free-solid-svg-icons';
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NotificationType} from "../../enumeration/notification-enum";
import {NotifierService} from "angular-notifier";

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

  loginForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]),
    password: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(50)])
  });

  constructor(private userService: UserService, private router: Router, private readonly notifier: NotifierService) {
  }

  ngOnInit(): void {

  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

  public onLogin() {
    if (!this.username.value || !this.password.value) {
      return;
    }
    const formData: FormData = this.userService.createFormData({
      'username': this.username.value,
      'password': this.password.value
    });
    this.userService.loginUser(formData).subscribe({
      next: () => {
        this.router.navigateByUrl('/home');
      }
    })
    this.notifier.notify(NotificationType.SUCCESS, 'Welcome, Username');
    this.subscriptions.push();
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
