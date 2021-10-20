package com.example.pmarbar1.app;
// ------------------------------------------------------------------
// Nombre del fichero: MainActivity.java
// Autor: Pere Márquez Barber
// Fecha: 15/10/21
// Fichero con la lógica de la app
// ------------------------------------------------------------------

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    //Variables estáticas creadas para usar más adelante
    // --------------------------------------------------------------
    //ETIQUETA_LOG, usada en los métodos log para diferencias y ordenar el Logcat
    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;

    // --------------------------------------------------------------
    //Objetos que usamos en el proyecto
    // --------------------------------------------------------------
    private Intent elIntentDelServicio = null;
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;
    // --------------------------------------------------------------
    //Métodos que usamos en el proyecto
    // --------------------------------------------------------------

   /* // --------------------------------------------------------------
    //buscarTodosLosDispositivosBTLE()<-
    // Este método escanea los beacons y muestra los resultados de la búsqueda, tanto errores como
    // lo que está haciendo en cada momento
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {


        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {

            //Resultados de la búsqueda
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            //En caso de fallo...
            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        //Empieza escaneo...
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        this.elEscanner.startScan( this.callbackDelEscaneo);

    } // ()*/

    // --------------------------------------------------------------
    //ScanResult->mostrarInformacionDispositivoBTLE()<-
    // Este método nos muestra los beacons encontrados y nos da toda la información obtenida del
    // beacon en el LogCat
    //Además, se ha aplicado un filtro para que solamente muestre nuestro beacon, y no los demás
    //o las búsquedas vacias
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();
        String filtroNombre =  bluetoothDevice.getName() + "";

        //Filtro de resultados, solamente nos muestra nuestro beacon
        //if (filtroNombre.equals("GTI-3A-PERE")){
            Log.d(ETIQUETA_LOG, " ****************************************************");
            Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
            Log.d(ETIQUETA_LOG, " ****************************************************");
            Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
            Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());

            Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
            Log.d(ETIQUETA_LOG, " rssi = " + rssi );

            Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
            Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

            TramaIBeacon tib = new TramaIBeacon(bytes);

            Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
            Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
            Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
            Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
            Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
            Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
            Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                    + tib.getiBeaconLength() + " ) ");
            Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
            Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
            Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                    + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
            Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                    + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
            Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
            Log.d(ETIQUETA_LOG, " ****************************************************");
        //}
    } // ()

    // --------------------------------------------------------------
    //String->buscarEsteDispositivoBTLE()<-
    //Nos muestra la información del escaneo
    //Aplica un filtro con el nobre del dispositivo que estamos buscando
    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado ) {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");

            }
        };

        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();

        ScanFilter scanFilterName = new ScanFilter.Builder().setDeviceName(dispositivoBuscado).build();
        filters.add(scanFilterName);
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
        this.elEscanner.startScan(filters, settings, this.callbackDelEscaneo );
        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos a escanear buscando: " + dispositivoBuscado );
    } // ()

    // --------------------------------------------------------------
    //detenerBusquedaDispositivosBTLE()->
    //Para el escaneo
    // Método llamado en botonDetenerServicioPulsado()
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        if ( this.callbackDelEscaneo == null ) {
            return;
        }

        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    //inicializarBlueTooth()->
    //Método que inicializa el bluetooth y nos muestra los pasos que va haciendo
    // --------------------------------------------------------------
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos adaptador BT ");

        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitamos adaptador BT ");

        bta.enable();

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): habilitado =  " + bta.isEnabled() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): estado =  " + bta.getState() );

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): obtenemos escaner btle ");

        this.elEscanner = bta.getBluetoothLeScanner();

        if ( this.elEscanner == null ) {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): Socorro: NO hemos obtenido escaner btle  !!!!");

        }

        Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): voy a perdir permisos (si no los tuviera) !!!!");

        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                    CODIGO_PETICION_PERMISOS);
        }
        else {
            Log.d(ETIQUETA_LOG, " inicializarBlueTooth(): parece que YA tengo los permisos necesarios !!!!");

        }
    } // ()

    // --------------------------------------------------------------
    //Al inicializar la aplicación
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");

    } // onCreate()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()

    //-------------------------------------------------------------------------
    //Métodos peticionario REST
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    //View->boton_enviar_pulsado()<-
    //Método en el que interactua el usuario al pulsar botón GET
    //Muestra en LogCat un JSON con todas las mediciones de la bbdd
    //Este método llama a la clase peticionarioREST
    //-------------------------------------------------------------------------
    public void boton_enviar_pulsado (View quien) {
        Log.d("clienterestandroid", "boton_enviar_pulsado");


        // ojo: creo que hay que crear uno nuevo cada vez
        PeticionarioREST elPeticionario = new PeticionarioREST();

        elPeticionario.hacerPeticionREST("GET",  "http://172.20.10.9:8000/api/mostrarMedidas", null,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d("Respuesta", "codigo respuesta= " + codigo + " <-> \n" + cuerpo);
                    }
                }
        );

    } // pulsado ()

    //-------------------------------------------------------------------------
    //View->boton_escribir_pulsado()->
    //Método en el que interactua el usuario al pulsar botón POST
    //Agrega una medición a la bbdd
    //Este método llama a la clase peticionarioREST
    //-------------------------------------------------------------------------
    public void boton_escribir_pulsado (View quien) {
        Log.d("clienterestandroid", "boton_escribir_pulsado");

        String cuerpoPost = "{" +
                "\"valor\" : " + 888 +
                "}";

        // ojo: creo que hay que crear uno nuevo cada vez
        PeticionarioREST elPeticionario = new PeticionarioREST();

        elPeticionario.hacerPeticionREST("POST",  "http://172.20.10.9:8000/api/agregar", cuerpoPost,
                new PeticionarioREST.RespuestaREST () {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        Log.d("RespuestaPOST", "codigo respuesta= " + codigo + " <-> \n" + cuerpo);
                    }
                }
        );

    } // pulsado ()

    // ---------------------------------------------------------------------------------------------
    // Botones servicio
    // ---------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    //View->botonArrancarServicioPulsado()->
    //Método en el que interactua el usuario al pulsar botón Arrancar servicio
    //Arranca servicio escucha beacon
    //Este método llama a inicializarBluetooth() y a buscarEsteDispositivoBTLE()
    //-------------------------------------------------------------------------
    public void botonArrancarServicioPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton arrancar servicio Pulsado" );

        if ( this.elIntentDelServicio != null ) {
            // ya estaba arrancado
            return;
        }

        Log.d(ETIQUETA_LOG, " MainActivity.constructor : voy a arrancar el servicio");

        this.elIntentDelServicio = new Intent(this, ServicioEscuharBeacons.class);

        this.elIntentDelServicio.putExtra("tiempoDeEspera", (long) 5000);
        startService( this.elIntentDelServicio );

        Log.d(ETIQUETA_LOG, " Inicializa búsqueda");
        inicializarBlueTooth();
        this.buscarEsteDispositivoBTLE("GTI-3A-PERE");

    } // ()

    //-------------------------------------------------------------------------
    //View->botonDetenerServicioPulsado()->
    //Método en el que interactua el usuario al pulsar botón Detener servicio
    //Detiene servicio escucha beacon
    //Este método llama a detenerBusquedaDispositivoBTLE
    //-------------------------------------------------------------------------
    public void botonDetenerServicioPulsado( View v ) {

        if ( this.elIntentDelServicio == null ) {
            // no estaba arrancado
            return;
        }

        stopService( this.elIntentDelServicio );

        this.elIntentDelServicio = null;

        Log.d(ETIQUETA_LOG, "boton detener servicio Pulsado" );

        this.detenerBusquedaDispositivosBTLE();


    } // ()

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------


} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------


