import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from './Servicios/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent implements OnInit {
  title = 'ProyectoIntegrado';
  isLogged: boolean = false;
  usuario: any = '';
  allowedRoutesWithoutLogin: string[] = ['/login', '/register'];

  constructor(private authService: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.checkLoginStatus();
      }
    });
  }

  ngOnInit(): void {
    this.checkLoginStatus();
  }

  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      //console.log('Decoded Token:', decodedToken); // Log the decoded token
      const currentTime = Math.floor(new Date().getTime() / 1000); // Get current time in seconds
      if (decodedToken.exp && decodedToken.exp > currentTime) {
        this.isLogged = true;
        this.usuario = decodedToken.sub;
      } else {
        this.isLogged = false;
        this.redirectIfNotAllowed();
      }
    } else {
      this.isLogged = false;
      this.redirectIfNotAllowed();
    }
  }

  decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decodedPayload = atob(payload);
      return JSON.parse(decodedPayload);
    } catch (error) {
      return null;
    }
  }

  redirectIfNotAllowed(): void {
    if (!this.isLogged && !this.allowedRoutesWithoutLogin.includes(this.router.url)) {
      this.router.navigate(['/login']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
