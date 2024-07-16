import { Component } from '@angular/core';
import { UserRegisterDto } from '../../services/models';
import { AuthenticationService } from '../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  userRegisterDto: UserRegisterDto = {
    email: "", 
    password: "",
    firstName: "",
    lastName: "", 
    dateOfbirth: ""
  };
  errorMsg: Array<string> = [];
  errorStatus:boolean = false;

  constructor(private _router:Router, private _authenticationService: AuthenticationService){}

  login() {
    this._router.navigate(["login"]);
  }

  register() {
    this._authenticationService.register({body: this.userRegisterDto}).subscribe({
      next: () => {
        this._router.navigate(["activate-account"]);
      }, 
      error: (err) => {
        console.log(err);
        if (err.error && err.error.errors) {
          this.errorStatus=true;
          this.errorMsg = err.error.errors.map((e: any) => e.defaultMessage);
        } else {
          this.errorMsg = ['An unexpected error occurred.'];
        }
      }
    }
    );
  }
}
