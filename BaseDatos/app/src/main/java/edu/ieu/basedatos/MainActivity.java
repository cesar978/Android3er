package edu.ieu.basedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.ieu.basedatos.modelo.AdminSqliteOpenHelper;

public class MainActivity extends AppCompatActivity {
    private final AdminSqliteOpenHelper adminDb=new AdminSqliteOpenHelper(this);
    private  static final String TABLE_NAME="articulos";
    private EditText etCodigo;
    private EditText etPrecio;
    private  EditText etDescripcion;

    private Button btnAlta;
    private Button btnBuscarPorCodigo;
    private  Button btnBaja;
    private Button btnActualizar;
    private Button btnBuscarPorDescripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCodigo = findViewById(R.id.et_codigo);
        etPrecio = findViewById(R.id.et_precio);
        etDescripcion = findViewById(R.id.et_descripcion);
        btnAlta = findViewById(R.id.btn_alta);
        btnBuscarPorCodigo = findViewById(R.id.btn_buscar_codigo);
        btnBaja = findViewById(R.id.btn_baja);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnBuscarPorDescripcion = findViewById(R.id.btn_buscar_descripcion);

        btnAlta.setOnClickListener(view -> {
            alta();


        });
        btnBuscarPorCodigo.setOnClickListener(view -> {
            buscarPorCodigo();
        });
        btnBaja.setOnClickListener(view -> {
            bajaArticulo();
        });
        btnActualizar.setOnClickListener(view -> acutalizarContenido());

        btnBuscarPorDescripcion.setOnClickListener(view -> {
                buscarDescripcion();
        });

    }

    private void buscarDescripcion() {
        SQLiteDatabase bd = adminDb.getWritableDatabase();
        String descri = etDescripcion.getText().toString();
        Cursor fila = bd.rawQuery(
                "select codigo,precio from articulos where descripcion='" + descri +"'", null);
        if (fila.moveToFirst()) {
            etCodigo.setText(fila.getString(0));
            etPrecio.setText(fila.getString(1));
        } else
            Toast.makeText(this, "No existe un artículo con dicha descripción",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }



    private void acutalizarContenido() {
        String codigo =etCodigo.getText().toString();
        Double precio =Double.parseDouble(etPrecio.getText().toString());
        String descripcion= etDescripcion.getText().toString();

        SQLiteDatabase db=adminDb.getWritableDatabase();
        if (codigo.length()>0){
            ContentValues content = new ContentValues();
            //content.put("codigo ",codigo);
            content.put("precio", precio);
            content.put("descripcion", descripcion);
            String where="codigo=?";
            String[] whereparams={codigo};

            int actualizados=db.update(TABLE_NAME,content, where,whereparams);
            if (actualizados>0){
                Toast.makeText(this,"actualizado el articulo con el codigo "+
                                codigo + "correctamente",Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(this,"codigo "+codigo+" no encontrado en la base de datos", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this,"el codigo no debe estar vacio", Toast.LENGTH_LONG).show();
        }
    }


    private void bajaArticulo() {
        String codigo=etCodigo.getText().toString();
        if (codigo.length()>0){
            SQLiteDatabase db= adminDb.getWritableDatabase();
            String where="codigo=?";//WHERE
            String[] stringArray= {codigo};//VALORES DEL WHERE
            int registros_borrados =db.delete(
                    TABLE_NAME,
                    where,
                    stringArray


            );
            if (registros_borrados>0){
                Toast.makeText(this, "articulo con codigo "+codigo+" 2borrado de la base de datos"
                ,Toast.LENGTH_LONG).show();
                etCodigo.setText("");
            }
            else{
                Toast.makeText(this,"Codigo "+ codigo+" no existe en la base de datos",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this,"el codigo no debe ser vacio",Toast.LENGTH_LONG).show();
        }
    }





    private void buscarPorCodigo() {
        String codigo=etCodigo.getText().toString();
        if (codigo.length()>0){
            SQLiteDatabase db= adminDb.getWritableDatabase();
            String where="codigo=?";//WHERE
            String[] stringArray= {codigo};//VALORES DEL WHERE
            Cursor cursor =db.query(
                    TABLE_NAME,
                    null,
                    where,
                    stringArray,
                    null,
                    null,
                    null

            );
            if (cursor.moveToNext()){
                @SuppressLint("Range")
                String descripcion=cursor.getString(cursor.getColumnIndex("descripcion"));
                @SuppressLint("Range")
                Double precio=cursor.getDouble(cursor.getColumnIndex("precio"));
                etPrecio.setText(precio.toString());
                etDescripcion.setText(descripcion.toString());

            }else{
                Toast.makeText(this,"Codigo "+ codigo+" no existe en la base de datos",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this,"el codigo no debe ser vacio",Toast.LENGTH_LONG).show();
        }
    }

    private void alta() {
        String codigo=etCodigo.getText().toString();
        Double precio= Double.parseDouble(etPrecio.getText().toString());
        String descripcion=etDescripcion.getText().toString();
        SQLiteDatabase db= adminDb.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("codigo",codigo);
        content.put("precio", precio);
        content.put("descripcion",descripcion);

        db.insert(TABLE_NAME, null, content);
        Toast.makeText(this,"Insertado el codigo "+ codigo+" en la base de datos",Toast.LENGTH_LONG).show();

    }
}