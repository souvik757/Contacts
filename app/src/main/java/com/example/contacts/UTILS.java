package com.example.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UTILS {
    public boolean usernameValidator(String username){
        // 1 . Shouldn't start with Caps .
        // 2 . Shouldn't start with special char's -> '-' , '!' , '@' , '#' , '$' , '%' , '^' , '&' , '*'
        // 3 . Min Length -> 3
        // 4 . Max Length -> 30
        // 5 . Special char count -> 1
        boolean condition1  = Character.isUpperCase(username.charAt(0)) ;             // 1 .
        boolean condition2  =
                (username.charAt(0) == '!' || username.charAt(0) == '@' ||
                        username.charAt(0) == '#' || username.charAt(0) == '$' ||
                        username.charAt(0) == '%' || username.charAt(0) == '^' ||
                        username.charAt(0) == '&' || username.charAt(0) == '*' ||
                        username.charAt(0) == '_')  ;                                        // 2 .
        boolean condition3AND4 = (username.length() < 3 || username.length() > 30) ; // 3 , 4 .
        int count = 0 ;
        for(int i = 0 ; i < username.length() ; i++){
            if(username.charAt(i) == '!' || username.charAt(i) == '@' ||
                    username.charAt(i) == '#' || username.charAt(i) == '$' ||
                    username.charAt(i) == '%' || username.charAt(i) == '^' ||
                    username.charAt(i) == '&' || username.charAt(i) == '*' ||
                    username.charAt(i) == '_')
                count++ ;
        }
        boolean condition5 = (count != 1) ;                                          // 5 .

        if(condition1 || condition2 || condition3AND4 || condition5)
            return true ;

        return true ;
    }

    public boolean passwordValidator(String password){
        //1. Min Length : 8
        //2. Max Length : 30
        if(password.length() < 8 || password.length() > 30) return false ;
        //3. At least   :  1  lowercase 'a' to 'z'
        boolean HasLowerCase = false ;
        int CountHasLowerCase = 0 ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isLowerCase(password.charAt(i))) {
                CountHasLowerCase++ ;
            }
        }
        HasLowerCase = CountHasLowerCase >= 1 ;
        //4. At least   :  1  Uppercase 'A' to 'Z'
        boolean HasUpperCase = false ;
        int CountHasUpperCase = 0 ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isUpperCase(password.charAt(i))) {
                CountHasUpperCase++ ;
            }
        }
        HasUpperCase = CountHasUpperCase >= 1 ;
        //5. At least   :  1  Numeric
        boolean HasNumeric = false ;
        for(int i = 0 ; i < password.length() ; i++){
            if(Character.isDigit(password.charAt(i)))
            {
                HasNumeric = true ;
                break ;
            }
        }
        //6. At least   :  1  special character '#' '@' '_' '*' '!' '$' '%' '&' '^'
        boolean HasSpecial = false ;
        for(int i = 0 ; i < password.length() ; i++){
            if(password.charAt(i) == '!' || password.charAt(i) == '@' ||
                    password.charAt(i) == '#' || password.charAt(i) == '$' ||
                    password.charAt(i) == '%' || password.charAt(i) == '^' ||
                    password.charAt(i) == '&' || password.charAt(i) == '*' ||
                    password.charAt(i) == '_'){
                HasSpecial = true ;
                break ;
            }
        }

        return HasLowerCase && HasUpperCase && HasNumeric && HasSpecial ;
    }
    public boolean nameValidator(String FN , String MN , String LN) {
        if (FN.length() == 0 || LN.length() <= 1) return false ;
        if (FN.length() != 1) {
            if (Character.isUpperCase(FN.charAt(FN.length() - 1)) || Character.isUpperCase(LN.charAt(LN.length() - 1)))
                return false;
            if ( Character.isLowerCase(FN.charAt(0)) || Character.isLowerCase(LN.charAt(0)) )
                return false ;
        }
        else
        {
            if ( Character.isLowerCase(FN.charAt(0)) || Character.isLowerCase(LN.charAt(0)) )
                return false ;
        }

        return true ;
    }
    public boolean phoneValidator(String PH) {
        if (PH.length() != 10) return false ;
        for (int i = 0 ; i < PH.length() ; i++) {
            if (!Character.isDigit(PH.charAt(i)))
                return false ;
        }
        return true ;
    }
    public boolean passwordConfirmation(String pw , String cpw) {
        if (pw.length() != cpw.length()) return false ;
        return pw.equals(cpw) ;
    }
    public String ParseName(String FN,String MN,String LN) {
        if (MN.length() == 0)
            return FN+" "+LN ;
        return FN+" "+MN+" "+LN ;
    }
    public boolean IsConnected(Context context){
        ConnectivityManager CM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE) ;
        NetworkInfo activeNetwork = CM.getActiveNetworkInfo() ;
        boolean HasConnection = activeNetwork != null && activeNetwork.isConnectedOrConnecting() ;
        return HasConnection ;
    }
    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}
