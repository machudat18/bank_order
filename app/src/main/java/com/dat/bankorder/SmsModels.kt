package com.dat.bankorder

class SmsModels {
    var id: String? = null
    var address: String? = null
    var msg: String? = null
    var readState //"0" for have not read sms and "1" for have read sms
            : String? = null
    var time: String? = null
     override fun toString(): String {
         return "SmsModels(id=$id, address=$address, msg=$msg, readState=$readState, time=$time)"
     }

 }