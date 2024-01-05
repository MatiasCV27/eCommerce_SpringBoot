package com.proyecto.ecommerce.model;

import java.util.Date;

public class Orden {

    private Integer id;
    private String numero;
    private Date fechaCreacion;
    private Date fechaRecibido;
    private double total;

    public Orden() {
    }

    public Orden(Integer id, String numero, Date fechaCreacion, Date fechaRecibido, double total) {
        this.id = id;
        this.numero = numero;
        this.fechaCreacion = fechaCreacion;
        this.fechaRecibido = fechaRecibido;
        this.total = total;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaRecibido() {
        return fechaRecibido;
    }
    public void setFechaRecibido(Date fechaRecibido) {
        this.fechaRecibido = fechaRecibido;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Orden{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaRecibido=" + fechaRecibido +
                ", total=" + total +
                '}';
    }
}
