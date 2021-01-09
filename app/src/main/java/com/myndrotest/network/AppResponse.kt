package com.myndrotest.network

import com.myndrotest.model.UserListModel

data class AppResponse (val status : Boolean,val message : String?,val data : UserListModel)