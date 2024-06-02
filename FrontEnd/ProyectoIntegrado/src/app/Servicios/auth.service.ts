import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = environment.puerto + "/api/auth";
  constructor(private http: HttpClient, private router: Router) { }

  registerConsumidor(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.url}/registerConsumidor`, user, { headers });
  }

  registerOfertante(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.url}/registerOfertante`, user, { headers });
  }

  registerOfertanteConsumidor(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.url}/registerOfertanteConsumidor`, user, { headers });
  }

  login(user: any): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.url}/login`, user, { headers }).pipe(
      tap(response => {
        // Guarda el token JWT en el sessionStorage
        sessionStorage.setItem('token', response.accesToken);
        this.router.navigate(['/home']);
      })
    );
  }

  logout(): void {
    // Elimina el token JWT del sessionStorage
    sessionStorage.removeItem('token');
  }
}
