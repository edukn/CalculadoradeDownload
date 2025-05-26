package com.edukian.calculadora_download.calculadoradedownload;

/**
 * Created by EduardoKNeto on 22/02/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;
import android.widget.Toast;

public class ContextoDados extends  SQLiteOpenHelper {

    private static final String NOME_BD = "Historico";
    private static final int VERSAO_BD = 2;
    private static final String LOG_TAG = "Historico";
    private final Context contexto;

    public ContextoDados(Context context) {
        super(context, NOME_BD, null, VERSAO_BD);
        this.contexto = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE IF NOT EXISTS historico (_id integer primary key autoincrement, " +
                "velocidade text, tamanho text, tipo_tam text, tipo_vel text, tempo text)";
        db.beginTransaction();

        try
        {
            // Cria a tabela e testa os dados
            ExecutarComandosSQL(db, sql);
            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            Log.e("Erro ao criar a tabela", e.toString());
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        
    }

    private void ExecutarComandosSQL(SQLiteDatabase db, String sql)
    {
        db.execSQL(sql);
    }

    public void Delete()
    {
        SQLiteDatabase db = getReadableDatabase();
        db.delete("historico", null, null);
    }


    public long InserirCalculo(String tamanho, String velocidade, String tipo_tam, String tipo_vel, String tempo)
    {
        SQLiteDatabase db = getReadableDatabase();

        try
        {
            ContentValues initialValues = new ContentValues();
            initialValues.put("tamanho", tamanho);
            initialValues.put("velocidade", velocidade);
            initialValues.put("tipo_tam", tipo_tam);
            initialValues.put("tipo_vel", tipo_vel);
            initialValues.put("tempo", tempo);
            return db.insert("historico", null, initialValues);
        }
        finally
        {
            db.close();
        }
    }

    public ContatosCursor RetornarContatos()
    {
        String sql = ContatosCursor.CONSULTA;
        SQLiteDatabase bd = getReadableDatabase();
        ContatosCursor cc = (ContatosCursor) bd.rawQueryWithFactory(new ContatosCursor.Factory(), sql, null, null);
        cc.moveToFirst();
        return cc;
    }



    //Classes adicionais para retornar dados

    public static class ContatosCursor extends SQLiteCursor
    {
        public static enum OrdenarPor{
            NomeCrescente,
            NomeDecrescente
        }

        private static final String CONSULTA = "SELECT * FROM historico ";

        private ContatosCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
        {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory
        {
            @Override
            public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query)
            {
                return new ContatosCursor(db, driver, editTable, query);
            }
        }


        public String getTamanho()
        {
            return getString(getColumnIndexOrThrow("tamanho"));
        }

        public String getVelocidade()
        {
            return getString(getColumnIndexOrThrow("velocidade"));
        }

        public String getTipo_Tam()
        {
            return getString(getColumnIndexOrThrow("tipo_tam"));
        }
        public String getTipo_Vel()
        {
            return getString(getColumnIndexOrThrow("tipo_vel"));
        }
        public String getTempo()
        {
            return getString(getColumnIndexOrThrow("tempo"));
        }
    }
}
