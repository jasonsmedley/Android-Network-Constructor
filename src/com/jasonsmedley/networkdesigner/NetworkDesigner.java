package com.jasonsmedley.networkdesigner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NetworkDesigner extends Activity 
{
    /** Called when the activity is first created. */
    public static final String LOG_TAG = "NETWORK DESIGNER";
	private String RouterName, RouterIPAddress, RouterSubnetAddress;
    private int PortNumber;
    @Override
	public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		//For toasts:
		final Context c = this;
		
        Button ok = (Button) this.findViewById(R.id.submit);
        
        //Run on button click
        ok.setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				Toast.makeText(c, "Button Pressed", Toast.LENGTH_SHORT);
				RouterName = ((EditText)findViewById(R.id.editroutername)).getText().toString();
				Log.v(LOG_TAG, "RouterName value: " + RouterName);
		        RouterIPAddress = ((EditText)findViewById(R.id.editip)).getText().toString();
		        RouterSubnetAddress = ((EditText)findViewById(R.id.editsubnet)).getText().toString();
		        String PortTemp = ((EditText)findViewById(R.id.portno)).getText().toString();
				try 
				{
					PortNumber = Integer.parseInt(PortTemp);
					Log.v(LOG_TAG, "PortTemp PARSED");
				} catch (NumberFormatException e) 
				{
					Log.v(LOG_TAG, "NUMBER FORMAT EXCEPTION");
					return;
				}
				newSubnet test = new newSubnet();
				if (test.checkRouterExists(RouterName))
				{
					//TODO: Find out why this isn't working!
					Log.v(LOG_TAG, "ROUTER ALREADY EXISTS");					
				}
				else
				{
					if (test.createRouter(RouterName))
					{
						Log.v(LOG_TAG, "ROUTER CREATED");
					}					
				}
				if (test.setPortInfo(RouterName, RouterIPAddress, RouterSubnetAddress, PortNumber))
				{
					Log.v(LOG_TAG, "PORT INFORMATION SET");
				}
				else
				{
					Log.v(LOG_TAG, "IP Address Already Exists");
				}
			}
		});
    }
}