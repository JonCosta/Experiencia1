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
    		HashMap params = getCoordenadas() ; //É criado um HashMap que possui os dados de coordenadas GPS do usuário
    			//Toast.makeText(this, "Teste:", Toast.LENGTH_LONG).show() ;

    		//Cria-se o objeto JSON a partir do HashMap
            JSONObject jsonParams = new JSONObject(params);
            
            //Obtém-se a 'reposta' da WebService, defindo o método a ser acessado e os parâmetros
        	JSONObject resp = HttpClient.SendHttpPost(this.getString(R.string.url_ws), jsonParams);
        	try {
				verTaxiProx(resp) ;
			} catch (JSONException e) {
				e.printStackTrace();
			}
    		break ;
    	}//Fecha switch
    }//Fecha onClick
    
    public HashMap getCoordenadas(){
    	
    	double lat = 0, lng = 0 ;
    	
    	HashMap params = new HashMap();
    	LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        String bestProvider = LM.getBestProvider(new Criteria(), true) ;
        
        //Se obtém o local mais recente marcado pelo GPS
        local = LM.getLastKnownLocation(bestProvider) ;
        
        //Obtém-se os valores de latitude e longitude
        lat = local.getLatitude() ;
        lng = local.getLongitude();
        
        //Valores são inseridos no HashMap
        params.put("latitude", lat);
    	params.put("longitude", lng);
    	
    	return params ;
    	
    }//Fecha getCoordenadas
    
    public void verTaxiProx(JSONObject params) throws JSONException{
    	
    	try{
    		
    		double tax1lng, tax1lat, tax2lng, tax2lat, tax3lng, tax3lat ;
    		String tax1placa, tax2placa, tax3placa ;
    		
    		//Obtém os táxis a partir do objeto JSON
    		
    		tax1lng = params.getDouble("tax1lng") ;
    		tax1lat = params.getDouble("tax1lat") ;
    		tax1placa = params.getString("tax1placa") ;
    		
    		tax2lng = params.getDouble("tax2lng") ;
    		tax2lat = params.getDouble("tax2lat") ;
    		tax2placa = params.getString("tax2placa") ;
    		
    		tax3lng = params.getDouble("tax3lng") ;
    		tax3lat = params.getDouble("tax3lat") ;
    		tax3placa = params.getString("tax3placa") ;
    		
        	//São criados objetos Location a partir dos dados de cada táxi
        	Location loc1 = new Location("Local 1");
        	Location loc2 = new Location("Local 2") ;
        	Location loc3 = new Location("Local 3") ;
        	 
    		
        	loc1.setLatitude(tax1lat) ;
        	loc1.setLongitude(tax1lng) ;
        	
        	loc2.setLatitude(tax2lat) ;
        	loc2.setLongitude(tax2lng) ;
        	
        	loc3.setLatitude(tax3lat) ;
        	loc3.setLongitude(tax3lng) ;
        	
        	if(local.distanceTo(loc1) < local.distanceTo(loc2) && local.distanceTo(loc1) < local.distanceTo(loc3)){
        		Toast.makeText(this, "Placa do Táxi mais próximo: " +tax1placa, Toast.LENGTH_LONG).show() ;
        	}else if(local.distanceTo(loc2) < local.distanceTo(loc1) && local.distanceTo(loc2) < local.distanceTo(loc3)){
        		Toast.makeText(this, "Placa do Táxi mais próximo: " +tax2placa, Toast.LENGTH_LONG).show() ;
        	}else if(local.distanceTo(loc3) < local.distanceTo(loc1) && local.distanceTo(loc3) < local.distanceTo(loc2)){
        		Toast.makeText(this, "Placa do Táxi mais próximo: " +tax3placa, Toast.LENGTH_LONG).show() ;
        	}else{
        		Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show() ;
        	}
    	
    	}catch(JSONException e){
    		e.printStackTrace() ;
    	}//Fecha catch
    	
    }//Fecha verTaxiProx
    
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
