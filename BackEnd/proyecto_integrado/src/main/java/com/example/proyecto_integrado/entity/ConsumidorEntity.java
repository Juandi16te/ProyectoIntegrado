package com.example.proyecto_integrado.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "consumidor", schema = "proyecto_integrado", catalog = "")
public class ConsumidorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "usuario")
    private String usuario;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "apellidos")
    private String apellidos;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "contrasena")
    private String contrasena;
    @Basic
    @Column(name = "foto")
    private String foto;
    @Basic
    @Column(name = "fecha_creacion_usuario")
    private Date fechaCreacionUsuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getFechaCreacionUsuario() {
        return fechaCreacionUsuario;
    }

    public void setFechaCreacionUsuario(Date fechaCreacionUsuario) {
        this.fechaCreacionUsuario = fechaCreacionUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsumidorEntity consumidor = (ConsumidorEntity) o;

        if (id != consumidor.id) return false;
        if (usuario != null ? !usuario.equals(consumidor.usuario) : consumidor.usuario != null) return false;
        if (nombre != null ? !nombre.equals(consumidor.nombre) : consumidor.nombre != null) return false;
        if (apellidos != null ? !apellidos.equals(consumidor.apellidos) : consumidor.apellidos != null) return false;
        if (email != null ? !email.equals(consumidor.email) : consumidor.email != null) return false;
        if (contrasena != null ? !contrasena.equals(consumidor.contrasena) : consumidor.contrasena != null) return false;
        if (foto != null ? !foto.equals(consumidor.foto) : consumidor.foto != null) return false;
        if (fechaCreacionUsuario != null ? !fechaCreacionUsuario.equals(consumidor.fechaCreacionUsuario) : consumidor.fechaCreacionUsuario != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(id);
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (apellidos != null ? apellidos.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (contrasena != null ? contrasena.hashCode() : 0);
        result = 31 * result + (foto != null ? foto.hashCode() : 0);
        result = 31 * result + (fechaCreacionUsuario != null ? fechaCreacionUsuario.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConsumidorEntity{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", foto='" + foto + '\'' +
                ", fechaCreacionUsuario=" + fechaCreacionUsuario +
                '}';
    }
}
