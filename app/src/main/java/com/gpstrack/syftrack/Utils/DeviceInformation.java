package com.gpstrack.syftrack.Utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Patterns;

import java.util.regex.Pattern;

public class DeviceInformation
{
    public static String GmailID(Context context)
    {
        String gmailid=null;
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                gmailid = account.name;
            }
            else
            {
                gmailid=null;
            }

        }
        return gmailid;

    }

    public static String phoneNumber(Context context)
    {
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        tMgr.getNetworkOperatorName();
        tMgr.getImei(1);
        return mPhoneNumber;
    }
}
