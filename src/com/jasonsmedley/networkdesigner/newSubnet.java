package com.jasonsmedley.networkdesigner;

//import android.app.Activity;
//import android.widget.Toast;

public class newSubnet
{
    // to signify current number of routers.
    private int current_routers = 0; 
    //String Array to Hold Network Information.
    private static String[][][] NAUsed = new String[100][4][100];

    //Create Router
    public boolean createRouter(String routername)
    {
        for (int count = 0; count < 100; count++)
        {
            //Look for Null
        	if(NAUsed[count][2][0] == null && !checkRouterExists(routername))
            {
                //When found place router name in array.
        		NAUsed[count][2][0] = routername;
                current_routers++;
                //Toast Router Created
                return true;
            }
        }
        return false;
    }
    
    private boolean checkRouterExists(String routername)
    {
    	for (int count = 0; count <100; count++)
    	{
    		if(NAUsed[count][2][0] == routername)
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public void deleteRouter(String routerid)
    {
        for (int count = 0; count < NAUsed.length; count++)
        {
            if ((routerid.equals(NAUsed[count][2][0])))
            {
                for (int count1 = 0; count < 4; count1++)
                {
                    NAUsed[count][count1][0] = null;
                    for(int count2 = 0; count2 <100; count2++)
                    {
                    	NAUsed[count][count1][count2] = null;
                    }
                }
                current_routers--;
                //Toast Router Deleted
                //Toast.makeText(this, "Router Deleted", Toast.LENGTH_LONG).show();
                
            }
        }
    }

    public boolean checkPortConnection(String router1, int router1port, String router2, int router2port)
    {
        String ipaddress1 = getPortIPAddress(router1, router1port);
        String subnet1 = getPortSubnetAddress(router1, router1port);
        String ipaddress2 = getPortIPAddress(router2, router2port);
        String subnet2 = getPortSubnetAddress(router2, router2port);
        if ((!ipaddress1.equals(ipaddress2)) && (subnet1.equals(subnet2)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void changeRouterName(String routerid, String newrouterid)
    {
        for (int count = 0; count < NAUsed.length; count++)
        {
            if ((routerid.equals(NAUsed[count][2][0])))
            {
                NAUsed[count][2][0] = newrouterid;
                //Toast Router Name Changed
                //Toast.makeText(this, "Router Name Changed", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    public boolean setPortInfo(String routerid, String IPAddress, String subnet, int portNumber)
    {
        if (!checkIPExist(IPAddress))
        {
            for (int count = 0; count < NAUsed.length; count++)
            {
                if ((routerid.equals(NAUsed[count][2][0])))
                {
                    NAUsed[count][1][portNumber] = IPAddress;
                    Subnet instance = new Subnet();
                    instance.setIPAddress(IPAddress);
                    instance.setSubnetMask(subnet);
                    NAUsed[count][0][portNumber] = instance.getSubnetAddress();
                    return true;
                    //Toast Port Information Set
                    //Toast.makeText(this, "Port Information Set", Toast.LENGTH_LONG).show();
                }
            }
        }
		return false;
    }

    public String getPortIPAddress(String routerid, int portid)
    {
        for (int count = 0; count < NAUsed.length; count++)
        {
            if ((routerid.equals(NAUsed[count][2][0])))
            {
                return NAUsed[count][1][portid];
            }
        }
        return null;
    }

    public String getPortSubnetAddress(String routerid, int portid)
    {
        for (int count = 0; count < NAUsed.length; count++)
        {
            if ((routerid.equals(NAUsed[count][2][0])))
            {
                return NAUsed[count][0][portid];
            }
        }
        return null;
    }

    private static boolean checkIPExist(String ipaddress)
    {
        for (int count = 0; count < NAUsed.length; count++)
        {
            for (int innercount = 0; innercount < NAUsed.length; innercount++)
            {
                if (ipaddress.equals(NAUsed[count][1][innercount]))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
