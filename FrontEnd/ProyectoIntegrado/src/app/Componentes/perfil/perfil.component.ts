import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ConsumidorService } from '../../Servicios/consumidor.service';
import { OfertanteService } from '../../Servicios/ofertante.service';
import { CommonModule } from '@angular/common';
import { StarRatingComponent } from '../star-rating/star-rating.component';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, StarRatingComponent],
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {
  user = {
    id: '',
    usuario: '',
    nombre: '',
    apellidos: '',
    email: '',
    contrasena: '',
    foto: '',
    valoracion: 0,
    descripcion: '',
    fechaCreacionUsuario: ''
  };
  fechaCreacionConsumidor: any = '';
  isOfertante: boolean = false;
  isConsumidor: boolean = false;
  idOfertante: any = 0;
  idConsumidor: any = 0;
  usuario: any = '';
  decodedToken: any = null;
  showOverlay: boolean = false;

  constructor(
    private authService: AuthService,
    private consumidorService: ConsumidorService,
    private ofertanteService: OfertanteService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.usuario = params.get('usuario');
    });
    this.loadUserProfile();
    this.checkLoginStatus();
  }

  loadUserProfile(): void {
    this.showOverlay = true;
    this.ofertanteService.existByUsuario(this.usuario).subscribe({
      next: (response) => {
        if (response) {
          this.isOfertante = true;
          this.showOverlay = true;
          this.ofertanteService.getByUsuario(this.usuario).subscribe({
            next: (response) => {
              this.showOverlay = false;
              this.user = response;
              this.user.foto = this.convertBase64ToImage(this.user.foto); // Convertir base64 a imagen URL
            },
            error: (error) => {
              this.showOverlay = false;
              console.error('Error', error);
              this.router.navigate(['/login']);
            }
          });
        }
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
    this.showOverlay = true;
    this.consumidorService.existByUsuario(this.usuario).subscribe({
      next: (response) => {
        if (response) {
          this.isConsumidor = true;
          this.showOverlay = true;
          this.consumidorService.getByUsuario(this.usuario).subscribe({
            next: (response) => {
              this.showOverlay = false;
              if (!this.isOfertante) {
                this.user = response;
                this.user.foto = this.convertBase64ToImage(this.user.foto); // Convertir base64 a imagen URL
              }
              this.fechaCreacionConsumidor = response.fechaCreacionUsuario;
            },
            error: (error) => {
              this.showOverlay = false;
              console.error('Error', error);
              this.router.navigate(['/login']);
            }
          });
        }
      },
      error: (error) => {
        this.showOverlay = false;
        console.error('Error', error);
        this.router.navigate(['/login']);
      }
    });
  }

  convertBase64ToImage(base64String: string): string {
    // Verifica si el base64String ya contiene el encabezado
    if (!base64String.startsWith('data:image')) {
      return `data:image/jpeg;base64,${base64String}`;
    }
    return base64String;
  }

  //JWT
  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      const currentTime = Math.floor(new Date().getTime() / 1000);
      if (decodedToken.exp && decodedToken.exp > currentTime) {
        if (decodedToken.sub === this.user.usuario) {
        }
      } else {
        this.authService.logout();
      }
    } else {
      this.authService.logout();
    }
  }

  decodeToken(token: string): any {
    try {
      const payload = token.split('.')[1];
      const decodedPayload = atob(payload);
      return JSON.parse(decodedPayload);
    } catch (error) {
      console.error('Error decoding token', error);
      return null;
    }
  }
}
