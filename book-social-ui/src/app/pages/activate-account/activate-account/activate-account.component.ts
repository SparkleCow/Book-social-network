import { Component } from '@angular/core';
import { AuthenticationService } from '../../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.css'
})
export class ActivateAccountComponent {

  message:string = "";
  isOkay:boolean = true;
  submitted:boolean = false;

  constructor(private _router:Router, private _authenticationService:AuthenticationService){}

  onCodeCompleted(token: string) {
    this.confirmAccount(token);
  }

  confirmAccount(token:string){
    this._authenticationService.validate({token: token}).subscribe({
      next: () => {
        this.message = "Your account has been sucessfully activated"
        this.submitted = true;
        this.isOkay = true;
      },
      error: () => {
        this.message = "Activation failed. Token has been expired or invalid";
        this.submitted = true;
        this.isOkay = false;
      }
    })
  }

  returnToLogin() {
    this._router.navigate(["login"]);
  }
}
