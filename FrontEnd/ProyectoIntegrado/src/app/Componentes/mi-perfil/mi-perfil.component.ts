import { Component } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { ConsumidorService } from '../../Servicios/consumidor.service';
import { OfertanteService } from '../../Servicios/ofertante.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { StarRatingComponent } from '../star-rating/star-rating.component';

@Component({
  selector: 'app-mi-perfil',
  standalone: true,
  imports: [CommonModule, StarRatingComponent],
  templateUrl: './mi-perfil.component.html',
  styleUrl: './mi-perfil.component.css'
})
export class MiPerfilComponent {
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
  decodedToken: any = null;
  showOverlay: boolean = false;

  constructor(
    private authService: AuthService,
    private consumidorService: ConsumidorService,
    private ofertanteService: OfertanteService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.checkLoginStatus();
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    if (this.isOfertante) {
      this.showOverlay = true;
      this.ofertanteService.getById(this.idOfertante).subscribe({
        next: (response) => {
          this.showOverlay = false;
          this.user = response;
          this.user.foto = this.convertBase64ToImage(this.user.foto); // Convertir base64 a imagen URL
        },
        error: (error) => {
          this.showOverlay = false;
          this.router.navigate(['/login']);
        }
      });
    }
    if (this.isConsumidor) {
      this.showOverlay = true;
      this.consumidorService.getById(this.idConsumidor).subscribe({
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
          this.router.navigate(['/login']);
        }
      });
    }
  }

  convertBase64ToImage(base64String: string): string {
    // Verifica si el base64String ya contiene el encabezado
    if (!base64String.startsWith('data:image')) {
      return `data:image/jpeg;base64,${base64String}`;
    }
    return base64String;
  }

  editarPerfil(): void {
    this.router.navigate([`/perfil/edit/${this.idOfertante}/${this.idConsumidor}`]);
  }

  hacerseOfertante(): void {
    if (confirm("¡Confirma para hacerte ofertante también! 😎")) {
      this.showOverlay = true;
      this.ofertanteService.post(this.user).subscribe({
        next: (response) => {
          this.showOverlay = false;
        },
        error: (error) => {
          this.showOverlay = false;
        }
      });
      alert("Ahora eres ofertante y consumidor 🥳, confirma para volver a iniciar sesión")
      this.router.navigate(['/login']);
    }
  }

  hacerseConsumidor(): void {
    if (confirm("¡Confirma para hacerte consumidor también! 😎")) {
      this.showOverlay = true;
      this.consumidorService.post(this.user).subscribe({
        next: (response) => {
          this.showOverlay = false;
        },
        error: (error) => {
          this.showOverlay = false;
        }
      });
      alert("Ahora eres ofertante y consumidor 🥳, confirma para volver a iniciar sesión")
      this.router.navigate(['/login']);
    }
  }

  dejarOfertante(): void {
    if (confirm("¿Seguro que quieres dejar de ser ofertante 😕?")) {
      this.showOverlay = true;
      this.ofertanteService.delete(this.idOfertante).subscribe({
        next: (response) => {
          this.showOverlay = false;
        },
        error: (error) => {
          this.showOverlay = false;
        }
      });
      alert("Ahora solo eres consumidor, confirma para volver a iniciar sesión")
      this.router.navigate(['/login']);
    }
  }

  dejarConsumidor(): void {
    if (confirm("¿Seguro que quieres dejar de ser consumidor 😕?")) {
      this.showOverlay = true;
      this.consumidorService.delete(this.idConsumidor).subscribe({
        next: (response) => {
          this.showOverlay = false;
        },
        error: (error) => {
          this.showOverlay = false;
        }
      });
      alert("Ahora solo eres ofertante, confirma para volver a iniciar sesión")
      this.router.navigate(['/login']);
    }
  }

  eliminarPerfil(): void {
    if (confirm("¿Seguro que quieres eliminar tu cuenta 😢?")) {
      if (this.isOfertante) {
        this.showOverlay = true;
        this.ofertanteService.delete(this.idOfertante).subscribe({
          next: (response) => {
            this.showOverlay = false;
          },
          error: (error) => {
            this.showOverlay = false;
          }
        });
      }
      if (this.isConsumidor) {
        this.showOverlay = true;
        this.consumidorService.delete(this.idConsumidor).subscribe({
          next: (response) => {
            this.showOverlay = false;
          },
          error: (error) => {
            this.showOverlay = false;
          }
        });
      }
      this.authService.logout();
      alert("Te echaremos de menos 😭")
      this.router.navigate(['/login']);
    }
  }

  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      const currentTime = Math.floor(new Date().getTime() / 1000);
      if (decodedToken.exp && decodedToken.exp > currentTime) {
        if (decodedToken.authorities.includes('OFERTANTE')) {
          this.isOfertante = true;
          this.idOfertante = decodedToken.idOfertante;
        }
        if (decodedToken.authorities.includes('CONSUMIDOR')) {
          this.isConsumidor = true;
          this.idConsumidor = decodedToken.idConsumidor;
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
      return null;
    }
  }

}
