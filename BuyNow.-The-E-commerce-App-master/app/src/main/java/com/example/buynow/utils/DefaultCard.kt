package com.example.buynow.utils

import android.app.Activity

import android.content.Context
import android.content.SharedPreferences

object DefaultCard {

    const val DEFAULTCARDNAME = "DefaultCreditCard"


    fun Activity.CreateDefCard(cardNumber:String, boolean: Boolean){

        val defaultCard = getSharedPreferences(DEFAULTCARDNAME, Context.MODE_PRIVATE)//only for this application
        if(!defaultCard.getBoolean("isHaveDefaultCard",true)){
            val editor:SharedPreferences.Editor =  defaultCard.edit()//create editor object
            editor.putBoolean("isHaveDefaultCard",boolean)//according to the data type we've to use put
            editor.putString("cardNumber",cardNumber)
            editor.apply()//apply the Asynchronous method for better performance,save data in the xml file
        }
    }


    //read the data  file using below
    fun Activity.GetDefCard():String{
        var cardNumber: String = ""
        val defaultCard = getSharedPreferences(DEFAULTCARDNAME, Context.MODE_PRIVATE)
        cardNumber = defaultCard.getString("cardNumber","You Have No Cards").toString()//get string method id using here
        return cardNumber
    }

}