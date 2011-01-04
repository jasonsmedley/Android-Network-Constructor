package com.jasonsmedley.networkdesigner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NetworkDesigner extends Activity {
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
        ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) 
			{
				RouterName = ((EditText)findViewById(R.id.editroutername)).toString();
				Log.v(LOG_TAG, "RouterName value: " + RouterName);
		        RouterIPAddress = ((EditText)findViewById(R.id.editip)).getText().toString();
		        RouterSubnetAddress = ((EditText)findViewById(R.id.editsubnet)).getText().toString();
		        String PortTemp = ((EditText)findViewById(R.id.portno)).getText().toString();
				try {
					PortNumber = Integer.parseInt(PortTemp);
				} catch (NumberFormatException e) {
					Toast.makeText(c, "You entered an invalid Port Number!", Toast.LENGTH_SHORT);
					return;
				}
				newSubnet test = new newSubnet();
				if (test.createRouter(RouterName))
				{
					Toast.makeText(c, "Router Created", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(c, "Router Already Exist", Toast.LENGTH_SHORT).show();
				}
				if (test.setPortInfo(RouterName, RouterIPAddress, RouterSubnetAddress, PortNumber))
				{
					Toast.makeText(c, "Port Information Set", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(c, "That IP Address Already Exists", Toast.LENGTH_LONG).show();
				}
			}
		});
    }
}