import { Component } from '@angular/core';
import { UserLoginDto, UserResponseDto } from '../../services/models';
import { AuthenticationService } from '../../services/services';
import { Router } from '@angular/router';
import { error } from 'console';
import { TokenService } from '../../services/services/token.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  
  constructor(private _router:Router, private _authService:AuthenticationService, private _tokenService:TokenService){}

  userLogin: UserLoginDto = {email: "", password:""}
  errorMsg: Array<string> = []

  login():void {
    this._authService.login({body: this.userLogin}).subscribe({      
      //Save token
      next: (res: UserResponseDto):void => {
        this._tokenService.token = res.token as string;
        this._router.navigate(["/books"]);
      },
      error: (err):void => {
        console.log(err);
        if (err.error && err.error.errors) {
          this.errorMsg = err.error.errors.map((e: any) => e.defaultMessage);
        }else if(err.error.businessErrorDescription) {
          this.errorMsg = [err.error.businessErrorDescription];
        }else{
          this.errorMsg = ['An unexpected error occurred.'];
        }
      }
    })
  }

  register():void {
    this._router.navigate(["/register"]);
  }

  saveToken(token:string){
    localStorage.setItem("jwtToken", token)
  }
}
