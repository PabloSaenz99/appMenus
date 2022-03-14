package ucm.appmenus.recyclers;

/**
 * Interfaz implementada por los ViewHolders usados en RecyclerAdapter
 * */
public interface IReclycerElement<ELEMENT> {
    /**
     * Debe asignar los datos a una variable del ViewHolder y modificar las vistas
     * Por ejemplo si ELEMENT es un String que se muestra en un TextView, debe hacer:
     *      textView.setText(data);
     * */
    void setDatos(ELEMENT data);

    /**
     * @return datos de la variable del ViewHolder que fueron almacenados por setDatos
     * Puede no ser utilizada
     * */
    ELEMENT getDatos();
}
