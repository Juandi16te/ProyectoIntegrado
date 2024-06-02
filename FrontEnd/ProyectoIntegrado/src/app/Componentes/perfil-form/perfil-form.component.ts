import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Servicios/auth.service';
import { ConsumidorService } from '../../Servicios/consumidor.service';
import { OfertanteService } from '../../Servicios/ofertante.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-perfil-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil-form.component.html',
  styleUrls: ['./perfil-form.component.css']
})
export class PerfilFormComponent implements OnInit {
  user = {
    id: '',
    usuario: '',
    nombre: '',
    apellidos: '',
    email: '',
    contrasena: '',
    foto: '',
    valoracion: '',
    descripcion: ''
  };
  isOfertante: boolean = false;
  isConsumidor: boolean = false;
  idOfertante: any = 0;
  idConsumidor: any = 0;
  imagenSeleccionada: string | ArrayBuffer | null = null;
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
      this.idOfertante = params.get('idOfertante');
      this.idConsumidor = params.get('idConsumidor');
    });
    if (this.idOfertante != 0) {
      this.isOfertante = true;
    }
    if (this.idConsumidor != 0) {
      this.isConsumidor = true;
    }

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
          if (this.user.foto != 'data:image/jpeg;base64,' || this.user.foto.length == 0) {
            this.imagenSeleccionada = this.user.foto;
          }
        },
        error: (error) => {
          this.showOverlay = false;
          console.error('Error', error);
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
            if (this.user.foto != 'data:image/jpeg;base64,' || this.user.foto.length == 0) {
              this.imagenSeleccionada = this.user.foto;
            }
          }
        },
        error: (error) => {
          this.showOverlay = false;
          console.error('Error', error);
        }
      });
    }
  }

  async cargarImagen(event: any) {
    const archivo = event.target.files[0];
    const extension = archivo.name.split('.').pop().toLowerCase(); // Obtener la extensión del archivo
    if (extension === 'jpg' || extension === 'jpeg' || extension === 'png' || extension === 'gif') {
      const reader = new FileReader();
      reader.readAsDataURL(archivo);
      reader.onload = () => {
        this.imagenSeleccionada = reader.result as string;
        this.user.foto = this.imagenSeleccionada; // Guardar la imagen en base64 en el objeto user
      };
    } else {
      // Mostrar mensaje de error o realizar alguna acción si el archivo no es una imagen
      alert('El archivo seleccionado no es una imagen 🧐');
    }
  }

  actualizarPerfil(): void {
    if (this.isOfertante) {
      this.showOverlay = true;
      this.ofertanteService.put(this.user).subscribe({
        next: () => {
          this.showOverlay = false;
          console.log('Ofertante actualizado correctamente');
          if (this.isConsumidor) {
            this.showOverlay = true;
            this.user.id = this.idConsumidor;
            this.consumidorService.put(this.user).subscribe({
              next: () => {
                this.showOverlay = false;
                if (this.isOfertante == false) {
                  console.log('Consumidor actualizado correctamente');
                  this.router.navigate(['/miPerfil']);
                }
              },
              error: (error) => {
                this.showOverlay = false;
                alert('Ya existe un usuario con ese nombre de usuario 😕');
              }
            });
            this.user.id = this.idOfertante;
          }
          this.router.navigate(['/miPerfil']);
        },
        error: (error) => {
          this.showOverlay = false;
          alert('Ya existe un usuario con ese nombre de usuario 😕');
        }
      });
    } else {
      this.showOverlay = true;
      this.consumidorService.put(this.user).subscribe({
        next: () => {
          this.showOverlay = false;
          if (this.isOfertante == false) {
            console.log('Consumidor actualizado correctamente');
            this.router.navigate(['/miPerfil']);
          }
        },
        error: (error) => {
          this.showOverlay = false;
          alert('Ya existe un usuario con ese nombre de usuario 😕');
        }
      });
    }
  }

  borrarImagen(): void {
    this.user.foto = '';
    this.imagenSeleccionada = null;
  }

  checkLoginStatus(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = this.decodeToken(token);
      const currentTime = Math.floor(new Date().getTime() / 1000);
      if (decodedToken.exp && decodedToken.exp > currentTime) {
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
