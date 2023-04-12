import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { PasswordReset } from "../../interface/passwordReset";
import { ActivatedRoute } from "@angular/router";
import { Store } from "@ngrx/store";
import { changePassword } from "../../store/action/user.action";
import { take } from "rxjs";
import { checkPasswordEquality, passwordShowHideOption, removeListenersOnDestroy } from "../auth.index";

@Component({
  selector: 'app-password-change',
  templateUrl: './password.change.component.html',
  styleUrls: ['./password.change.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PasswordChangeComponent implements OnInit, OnDestroy {
  token: string | undefined;

  constructor(private activatedRoute: ActivatedRoute, private store: Store) {
  }

  passwordReset = new FormGroup({
    password: new FormControl('', [Validators.required,
      Validators.minLength(6), Validators.maxLength(50)]),
    confirmPassword: new FormControl('', [Validators.required,
      Validators.minLength(6), Validators.maxLength(50)])
  });

  ngOnInit(): void {
    passwordShowHideOption();

    this.activatedRoute.queryParams.pipe(
      take(1),
    ).subscribe(params => {
      this.token = params['token'];
    })
  }

  onConfirmPasswordChange() {
    return this.password.value === this.confirmPassword.value;
  }

  get password() {
    return this.passwordReset.get('password');
  }

  get confirmPassword() {
    return this.passwordReset.get('confirmPassword');
  }

  onSubmit() {
    if (!checkPasswordEquality(this.password.value, this.confirmPassword.value)) {
      return;
    }

    const data: PasswordReset = {
      password: this.password.value,
      confirmPassword: this.confirmPassword.value,
      token: this.token
    };

    this.store.dispatch(changePassword({ data }));
  }

  ngOnDestroy(): void {
    removeListenersOnDestroy();
  }
}
