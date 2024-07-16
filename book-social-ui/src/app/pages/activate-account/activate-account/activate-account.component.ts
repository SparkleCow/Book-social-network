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
    throw new Error('Method not implemented.');
  }
}
