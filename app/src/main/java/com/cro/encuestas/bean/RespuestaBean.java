package com.cro.encuestas.bean;

/**
 * Created by L4D3N on 20/02/2016.
 */
public class RespuestaBean {

    public int getRespuestaID() {
        return respuestaID;
    }

    public void setRespuestaID(int respuestaID) {
        this.respuestaID = respuestaID;
    }

    public int getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(int preguntaID) {
        this.preguntaID = preguntaID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    int respuestaID;
        int preguntaID;
        String descripcion;
}
