import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user = {
    user: '',
    password: ''
  };
  showOverlay: boolean = false;


  constructor(private authService: AuthService, private router: Router) {
    this.authService.logout();
  }

  camposCompletos(): boolean {
    return this.user.user != '' && this.user.password != '';
  }

  login(): void {
    this.showOverlay = true;
    this.authService.login(this.user).subscribe({
      next: (response) => {
        this.showOverlay = false;
      },
      error: (error) => {
        this.showOverlay = false;
        alert("Usuario o contraseña incorrecto 😕");
      }
    });
  }
}
