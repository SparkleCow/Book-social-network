import { Component } from '@angular/core';
import { UserLoginDto, UserResponseDto } from '../../services/models';
import { AuthenticationService } from '../../services/services';
import { Router } from '@angular/router';
import { error } from 'console';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
  constructor(private _router:Router, private _authService:AuthenticationService){}

  userLogin: UserLoginDto = {email: "", password:""}
  errorMsg: Array<string> = []

  login():void {
    this._authService.login({body: this.userLogin}).subscribe({      
      //Save token
      next: (res: UserResponseDto):void => {
        this._router.navigate(["/books"]);
      },
      error: (err):void => {
        console.log(err);
      }
    })
  }

  register():void {
    this._router.navigate(["/register"]);
  }

  saveToken(token:string){
    
  }
}
