package br.com.experiencia1;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {
	
	private Location local ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onClick(View v){
    	switch(v.getId()){
    	case R.id.btnInicio:
    		String placa = null ;
    		//É criado um HashMap que possui os dados de coordenadas GPS do usuário
    		HashMap<String, Double> params = getCoordenadas() ; 

    		//Cria-se o objeto JSON a partir do HashMap
            JSONObject jsonParams = new JSONObject(params);
            
            //Obtém-se a 'reposta' da WebService, defindo o método a ser acessado e os parâmetros
        	JSONObject resp = HttpClient.SendHttpPost(this.getString(R.string.url_ws), jsonParams);
        	
        	try {
				placa = resp.getString("Placa") ;
			} catch (JSONException e) {
				e.printStackTrace();
			}
        	
        	Toast.makeText(this, "Placa: "+placa, Toast.LENGTH_LONG).show() ;
        	
    		break ;
    	}//Fecha switch
    }//Fecha onClick
    
    public HashMap<String, Double> getCoordenadas(){
    	
    	double lat = 0, lng = 0 ;
    	
    	HashMap<String, Double> params = new HashMap<String, Double>();
    	LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        String bestProvider = LM.getBestProvider(new Criteria(), true) ;
        
        //Se obtém o local mais recente marcado pelo GPS
        local = LM.getLastKnownLocation(bestProvider) ;
        
        //Obtém-se os valores de latitude e longitude
        lat = local.getLatitude() ;
        lng = local.getLongitude() ;
        
        //Log.d("TESTE", "Lat: "+lat+ " Lng: "+log) ;
        
        //Valores são inseridos no HashMap
        params.put("latitude", lat);
    	params.put("longitude", lng);
    	
    	return params ;
    	
    }//Fecha getCoordenadas
    
    /*MÉTODOS DE LOCATION LISTENER*/
	@Override
	public void onLocationChanged(Location location) {
		this.local = location ;
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}
