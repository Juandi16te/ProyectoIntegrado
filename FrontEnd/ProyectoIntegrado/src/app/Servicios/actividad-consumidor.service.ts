import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ActividadConsumidorService {

  private url = environment.puerto + "/api/actividadConsumidor";
  constructor(private http: HttpClient) { }

  get(): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.url}/get`, { headers });
  }

  getById(actividadConsumidor: any): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.url}/get/${actividadConsumidor.idActividad}/${actividadConsumidor.idConsumidor}`, { headers });
  }

  post(actividadConsumidor: any): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.post<any>(`${this.url}/post`, actividadConsumidor, { headers });
  }

  exist(actividadConsumidor: any): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.url}/exist/${actividadConsumidor.idActividad}/${actividadConsumidor.idConsumidor}`, { headers });
  }

  put(actividadConsumidor: any): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.put<any>(`${this.url}/put`, actividadConsumidor, { headers });
  }

  delete(actividadConsumidor: any): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
    return this.http.delete<any>(`${this.url}/delete/${actividadConsumidor.idActividad}/${actividadConsumidor.idConsumidor}`, { headers });
  }
}
