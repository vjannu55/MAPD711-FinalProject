package com.codescafe.doctorappointment.utils;

public class Calling {
//    public static void Register_API(Activity activity, Common.APISuccessListener successListener, Common.APIErrorListener errorListener,
//                                    String first_name, String mobile, String email,String whatsappNumber,String user_type) {
//        String url= Utils.BASE_URL+"register";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//            Log.e("Register_res", response.toString());
//            successListener.onSuccessReceived(response.toString());
//        }
//        , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Utils.parseVolleyError(error, activity);
//                Log.e("onErrorResponse", error.toString());
//            }
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("name",""+first_name);
//                hashMap.put("email",""+email);
//                hashMap.put("mobile",""+whatsappNumber);
//                hashMap.put("whatsapp_number",""+mobile);
//                hashMap.put("usertype","master_distributer");
//                return hashMap;
//            }
//
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//    public static void Login_API(Activity activity, Common.APISuccessListener successListener, Common.APIErrorListener errorListener
//                                    ,String email, String password,String otp) {
//        String url= Utils.BASE_URL+"login.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//            successListener.onSuccessReceived(response.toString());
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//            Log.e("onErrorResponse", error.toString());
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("username",""+email);
//                hashMap.put("password",""+password);
//                hashMap.put("otp",""+otp);
//                Log.e("login", "getParams: "+new Gson().toJson(hashMap));
//                return hashMap;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//    public static void Send_OTP_API(Activity activity, Common.APISuccessListener successListener,
//                                   Common.APIErrorListener errorListener, String otp) {
//        String url= Utils.BASE_URL+"verify-otp";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            Log.e("OTP_res", response.toString());
//            successListener.onSuccessReceived(response.toString());
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//            Log.e("onErrorResponse", error.toString());
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//    public static void Get_User_Profile_API(Activity activity, Common.APISuccessListener successListener,
//                                   Common.APIErrorListener errorListener) {
//        String url= Utils.BASE_URL+"profile/19";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            Log.e("get_user_res", response.toString());
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(response);
//                if (jsonObject.getString("status").equals("200")){
//                    String ss = jsonObject.getString("user");
//                    Gson gson = new Gson();
//                    UserModel userModel = gson.fromJson(ss, UserModel.class);
//                    UserManager.setUserDetails(userModel,activity);
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            successListener.onSuccessReceived(response.toString());
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//            Log.e("onErrorResponse", error.toString());
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headerMap = new HashMap<String, String>();
//                headerMap.put("Content-Type", "application/json");
//                //headerMap.put("Authorization", "Bearer " + UserManager.getToken(activity));
//                return headerMap;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//    public static void GET_Roles_API(Activity activity, Common.APISuccessListener successListener,
//                                   Common.APIErrorListener errorListener) {
//        String url= Utils.BASE_URL+"roles";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            Log.e("get_user_roles", response.toString());
//            successListener.onSuccessReceived(response.toString());
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//            Log.e("onErrorResponse", error.toString());
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headerMap = new HashMap<String, String>();
//                headerMap.put("Content-Type", "application/json");
//                //headerMap.put("Authorization", "Bearer " + UserManager.getToken(activity));
//                return headerMap;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//    public static void Send_OTP(Activity activity, Common.APISuccessListener successListener,
//                                   Common.APIErrorListener errorListener,String username,String password) {
//        String url= Utils.BASE_URL+"sent-otp.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//            successListener.onSuccessReceived(response.toString());
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//
//        }){
//            @NonNull
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("username",""+username);
//                hashMap.put("password",""+password);
//                Log.e("otp", "getParams: "+new Gson().toJson(hashMap));
//                return hashMap;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//public static void Check_Wallet(Activity activity, Common.APISuccessListener successListener,
//                                   Common.APIErrorListener errorListener) {
//        String url= Utils.BASE_URL+"wallet-check";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
//            Log.e("get_walllet", response.toString());
//            successListener.onSuccessReceived(response.toString());
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(response);
//                if (jsonObject.getString("status").equals("true")){
//                    String ss = jsonObject.getString("wallet_balance");
//                    UserManager.setWallet(ss,activity);
//                    MainActivity.tv_balance.setText(""+UserManager.getWallet(activity));
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }, error -> {
//            Utils.parseVolleyError(error, activity);
//            Log.e("onErrorResponse", error.toString());
//        }){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("userid",""+UserManager.getUserDetails(activity).getUserid());
//                return hashMap;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//    }
//

}
