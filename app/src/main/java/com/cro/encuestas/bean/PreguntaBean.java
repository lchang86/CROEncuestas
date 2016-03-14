package com.cro.encuestas.bean;

/**
 * Created by L4D3N on 20/02/2016.
 */
public class PreguntaBean {

    int preguntaID;

    public int getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(int preguntaID) {
        this.preguntaID = preguntaID;
    }

    int encuestaID;
    String descripcion;

    public int getEncuestaID() {
        return encuestaID;
    }

    public void setEncuestaID(int encuestaID) {
        this.encuestaID = encuestaID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipoRespuestaID() {
        return tipoRespuestaID;
    }

    public void setTipoRespuestaID(int tipoRespuestaID) {
        this.tipoRespuestaID = tipoRespuestaID;
    }

    int tipoRespuestaID;




}
