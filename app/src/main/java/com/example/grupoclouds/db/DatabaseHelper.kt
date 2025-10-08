package com.example.grupoclouds.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ClubDeportivo.db"

        // --- Definiciones de Tablas y Columnas ---
        object TablaPersona {
            const val NOMBRE_TABLA = "Persona"
            const val COL_ID = "id_persona"
            const val COL_NOMBRE = "nombre"
            const val COL_APELLIDO = "apellido"
            const val COL_DNI = "dni"
            const val COL_FECHA_NACIMIENTO = "fecha_nacimiento"
        }
        object TablaAdministrador {
            const val NOMBRE_TABLA = "Administrador"
            const val COL_ID = "id_admin"
            const val COL_NOMBRE_USUARIO = "nombre_usuario"
            const val COL_CONTRASENA = "contrasena"
            const val COL_FECHA_REGISTRO = "fecha_registro"
            const val COL_ID_PERSONA = "id_persona" // Esta es la FK que conecta con la TablaPersona
        }
        object TablaSocio {
            const val NOMBRE_TABLA = "Socio"
            const val COL_ID = "id_socio"
            const val COL_FECHA_ALTA = "fecha_alta"
            const val COL_CUOTA_HASTA = "cuota_hasta"
            const val COL_TIENE_CARNET = "tiene_carnet"
            const val COL_FICHA_MEDICA = "ficha_medica"
            const val COL_ID_PERSONA = "id_persona"
        }
        object TablaNoSocio {
            const val NOMBRE_TABLA = "No_Socio"
            const val COL_ID = "id_no_socio"
            const val COL_ID_PERSONA = "id_persona"
        }
        object TablaCuota {
            const val NOMBRE_TABLA = "Cuota"
            const val COL_ID = "id_cuota"
            const val COL_MONTO = "monto"
            const val COL_FECHA_PAGO = "fecha_pago"
            const val COL_FECHA_VENCE = "fecha_vence"
            const val COL_MEDIO_PAGO = "medio_pago"
            const val COL_PROMOCION = "promocion"
            const val COL_ID_SOCIO = "id_socio"
        }

        object TablaActividad {
            const val NOMBRE_TABLA = "Actividad"
            const val COL_ID = "id_actividad"
            const val COL_NOMBRE = "nombre"
            const val COL_TIPO = "tipo"
            const val COL_PROFESOR = "profesor"
            const val COL_HORARIO = "horario"
            const val COL_CAPACIDAD = "capacidad"
            const val COL_COSTO_ACTIVIDAD = "costo_actividad"
        }

        // Tabla de unión para la relación muchos-a-muchos
        object TablaNoSocioActividad {
            const val NOMBRE_TABLA = "NoSocio_Actividad"
            const val COL_ID_NO_SOCIO = "id_no_socio"
            const val COL_ID_ACTIVIDAD = "id_actividad"
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys=ON;")

        val sqlCrearTablaPersona = """
            CREATE TABLE ${TablaPersona.NOMBRE_TABLA} (
                ${TablaPersona.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaPersona.COL_NOMBRE} TEXT NOT NULL,
                ${TablaPersona.COL_APELLIDO} TEXT,
                ${TablaPersona.COL_DNI} TEXT NOT NULL UNIQUE,
                ${TablaPersona.COL_FECHA_NACIMIENTO} TEXT
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaPersona)

        val sqlCrearTablaActividad = """
            CREATE TABLE ${TablaActividad.NOMBRE_TABLA} (
                ${TablaActividad.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaActividad.COL_NOMBRE} TEXT,
                ${TablaActividad.COL_TIPO} TEXT,
                ${TablaActividad.COL_PROFESOR} TEXT,
                ${TablaActividad.COL_HORARIO} TEXT,
                ${TablaActividad.COL_CAPACIDAD} INTEGER,
                ${TablaActividad.COL_COSTO_ACTIVIDAD} REAL
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaActividad)

        val sqlCrearTablaAdmin = """
            CREATE TABLE ${TablaAdministrador.NOMBRE_TABLA} (
                ${TablaAdministrador.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaAdministrador.COL_NOMBRE_USUARIO} TEXT NOT NULL UNIQUE,
                ${TablaAdministrador.COL_CONTRASENA} TEXT NOT NULL,
                ${TablaAdministrador.COL_FECHA_REGISTRO} TEXT DEFAULT CURRENT_TIMESTAMP,
                ${TablaAdministrador.COL_ID_PERSONA} INTEGER NOT NULL,
                FOREIGN KEY(${TablaAdministrador.COL_ID_PERSONA}) REFERENCES ${TablaPersona.NOMBRE_TABLA}(${TablaPersona.COL_ID})
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaAdmin)

        val sqlCrearTablaSocio = """
            CREATE TABLE ${TablaSocio.NOMBRE_TABLA} (
                ${TablaSocio.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaSocio.COL_FECHA_ALTA} TEXT,
                ${TablaSocio.COL_CUOTA_HASTA} TEXT,
                ${TablaSocio.COL_TIENE_CARNET} INTEGER,
                ${TablaSocio.COL_FICHA_MEDICA} INTEGER,
                ${TablaSocio.COL_ID_PERSONA} INTEGER NOT NULL,
                FOREIGN KEY(${TablaSocio.COL_ID_PERSONA}) REFERENCES ${TablaPersona.NOMBRE_TABLA}(${TablaPersona.COL_ID})
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaSocio)

        val sqlCrearTablaNoSocio = """
            CREATE TABLE ${TablaNoSocio.NOMBRE_TABLA} (
                ${TablaNoSocio.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaNoSocio.COL_ID_PERSONA} INTEGER NOT NULL,
                FOREIGN KEY(${TablaNoSocio.COL_ID_PERSONA}) REFERENCES ${TablaPersona.NOMBRE_TABLA}(${TablaPersona.COL_ID})
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaNoSocio)

        val sqlCrearTablaCuota = """
            CREATE TABLE ${TablaCuota.NOMBRE_TABLA} (
                ${TablaCuota.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${TablaCuota.COL_MONTO} REAL,
                ${TablaCuota.COL_FECHA_PAGO} TEXT,
                ${TablaCuota.COL_FECHA_VENCE} TEXT,
                ${TablaCuota.COL_MEDIO_PAGO} TEXT,
                ${TablaCuota.COL_PROMOCION} INTEGER,
                ${TablaCuota.COL_ID_SOCIO} INTEGER,
                FOREIGN KEY(${TablaCuota.COL_ID_SOCIO}) REFERENCES ${TablaSocio.NOMBRE_TABLA}(${TablaSocio.COL_ID})
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaCuota)

        val sqlCrearTablaNoSocioActividad = """
            CREATE TABLE ${TablaNoSocioActividad.NOMBRE_TABLA} (
                ${TablaNoSocioActividad.COL_ID_NO_SOCIO} INTEGER,
                ${TablaNoSocioActividad.COL_ID_ACTIVIDAD} INTEGER,
                PRIMARY KEY(${TablaNoSocioActividad.COL_ID_NO_SOCIO}, ${TablaNoSocioActividad.COL_ID_ACTIVIDAD}),
                FOREIGN KEY(${TablaNoSocioActividad.COL_ID_NO_SOCIO}) REFERENCES ${TablaNoSocio.NOMBRE_TABLA}(${TablaNoSocio.COL_ID}),
                FOREIGN KEY(${TablaNoSocioActividad.COL_ID_ACTIVIDAD}) REFERENCES ${TablaActividad.NOMBRE_TABLA}(${TablaActividad.COL_ID})
            )
        """.trimIndent()
        db?.execSQL(sqlCrearTablaNoSocioActividad)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Borrar las tablas en el orden inverso a la creación para evitar conflictos con las claves foráneas.
        db?.execSQL("DROP TABLE IF EXISTS ${TablaNoSocioActividad.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaCuota.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaNoSocio.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaSocio.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaAdministrador.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaActividad.NOMBRE_TABLA}")
        db?.execSQL("DROP TABLE IF EXISTS ${TablaPersona.NOMBRE_TABLA}")

        // Volver a crear la base de datos con el nuevo esquema
        onCreate(db)
    }
}