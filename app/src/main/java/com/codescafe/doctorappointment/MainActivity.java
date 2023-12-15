package com.codescafe.doctorappointment;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    TextView name;
//    CircleImageView profileImage;
//    Activity activity;
//    public static TextView tv_balance;
//    BottomNavigationView bottomNavigationView;
//    Fragment selectedFragment = null;
//
//    DrawerLayout drawerLayout;
//    ArrayList<NavModel> Nav_List = new ArrayList<>();
//    ImageView menu;
//    NavigationItemAdapter navigationItemAdapter;
//    RecyclerView navigation_rv;
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        activity = this;
//
//        In_it_Nav_list();
//        navigation_rv = findViewById(R.id.navigation_rv);
//        navigation_rv.setLayoutManager(new LinearLayoutManager(activity));
//        drawerLayout = findViewById(R.id.drawerOf);
//        menu = findViewById(R.id.icon_nav_menu);
//        name = findViewById(R.id.name);
//        tv_balance = findViewById(R.id.tv_balance);
//        profileImage = findViewById(R.id.profileImage);
//        try {
//            name.setText("" + UserManager.getUserDetails(activity).getName());
//            profileImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(activity, MyAccount.class));
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(nevigationSelected);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                new HomeFragment()).commit();
//        navigationItemAdapter = new NavigationItemAdapter(activity, Nav_List);
//        navigation_rv.setAdapter(navigationItemAdapter);
//        menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
//            }
//        });
//    }
//
//    @SuppressLint("NonConstantResourceId")
//    private final BottomNavigationView.OnNavigationItemSelectedListener nevigationSelected =
//            item -> {
//                switch (item.getItemId()) {
//                    case R.id.nav_pak:
//                        selectedFragment = new HomeFragment();
//                        break;
//                    case R.id.profile:
//                        selectedFragment = new ProfileFragment();
//                        break;
//
//
//                }
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                            selectedFragment).commit();
//                }
//                return true;
//            };
//
//    @Override
//    public void onBackPressed() {
//        if (bottomNavigationView.getSelectedItemId() == R.id.nav_pak) {
//            super.onBackPressed();
//            finish();
//        } else {
//            bottomNavigationView.setSelectedItemId(R.id.nav_pak);
//        }
//
//
//    }
//
//    private void getUpdate() {
//        getUserDetails(activity);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        getUpdate();
//    }
//
//    private void In_it_Nav_list() {
//        Nav_List.add(new NavModel("BASIC", "Home", R.drawable.home));
//        //   Nav_List.add(new NavModel("","Home",R.drawable.home));
//        Nav_List.add(new NavModel("", "Dashboard", R.drawable.home));
//        Nav_List.add(new NavModel("", "Add Money", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("FURTHER", "", R.drawable.home));
//        //Nav_List.add(new NavModel("","Transaction History",R.drawable.withdraw_history));
//        Nav_List.add(new NavModel("", "Service Charges", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("USER", "", R.drawable.home));
//        Nav_List.add(new NavModel("", "Add User", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "User List", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("PAN NO FIND", "", R.drawable.home));
//        Nav_List.add(new NavModel("", "Sent Request", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "Pan Find List", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "NSDL E-KYC APPLY", R.drawable.home));
//        Nav_List.add(new NavModel("", "NSDL Sent Request", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "NSDL Pan List", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "NSDL E-KYC CORRECTION", R.drawable.home));
//        Nav_List.add(new NavModel("", "NSDL CORRECTION Sent Request", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "NSDL E-KYC List", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "OTHERS", R.drawable.home));
//        Nav_List.add(new NavModel("", "Update App", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "Share App", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "Privacy Policy", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "Exit", R.drawable.ic_baseline_wallpaper_24));
//        Nav_List.add(new NavModel("", "Logout", R.drawable.ic_baseline_wallpaper_24));
//    }
//
//    public void Navigation_Click(NavModel model, int adapterPosition) {
//        switch (model.getTitle()) {
//            case "Dashboard":
//                Fragment homeFragment = new HomeFragment();
//                replaceFragment(homeFragment);
//                drawerLayout.close();
//                return;
//            case "Add Money":
//                Fragment moneyAddFragment = new MoneyAddFragment();
//                replaceFragment(moneyAddFragment);
//                drawerLayout.close();
//                return;
//            case "Service Charges":
//                Fragment serviceChargesFragment = new ServiceChargesFragment();
//                replaceFragment(serviceChargesFragment);
//                drawerLayout.close();
//                return;
//            case "Add User":
//                Fragment addUserFragment = new AddUserFragment();
//                replaceFragment(addUserFragment);
//                drawerLayout.close();
//                return;
//            case "User List":
//                Fragment usersListFragment = new UsersListFragment();
//                replaceFragment(usersListFragment);
//                drawerLayout.close();
//                return;
//            case "Sent Request":
//                Fragment panRequestFragment = new PanRequestFragment();
//                replaceFragment(panRequestFragment);
//                drawerLayout.close();
//                return;
//            case "Pan Find List":
//                Fragment panRequestListFragment = new PanRequestListFragment();
//                replaceFragment(panRequestListFragment);
//                drawerLayout.close();
//                return;
//            case "NSDL Sent Request":
//                Fragment nsdlekycFragment = new NSDLEKYCFragment();
//                replaceFragment(nsdlekycFragment);
//                drawerLayout.close();
//                return;
//            case "NSDL Pan List":
//                Fragment nsdlRequestListFragment = new NSDLRequestListFragment();
//                replaceFragment(nsdlRequestListFragment);
//                drawerLayout.close();
//                return;
//            case "NSDL CORRECTION Sent Request":
//                Fragment nsdlPanCorrectionFragment = new NSDLPanCorrectionFragment();
//                replaceFragment(nsdlPanCorrectionFragment);
//                drawerLayout.close();
//                return;
//            case "NSDL E-KYC List":
//                Fragment nsdlCorrectionListFragment = new NSDLCorrectionListFragment();
//                replaceFragment(nsdlCorrectionListFragment);
//                drawerLayout.close();
//                return;
//            case "Logout":
//                drawerLayout.close();
//                Logout();
//                return;
//            case "Update App":
//                drawerLayout.close();
//                Utils.gotoMarket(activity);
//                return;
//            case "Exit":
//                drawerLayout.close();
//                Exit_Fun();
//                return;
//            case "How To Play":
//                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Utils.companyApiModel.getYtvideo())));
//                return;
//            case "Support":
//                // startActivity(new Intent(getApplicationContext(), AboutApp.class));
//                drawerLayout.close();
//                return;
//            case "Share App":
//                Share_App();
//                drawerLayout.close();
//                return;
//        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                drawerLayout.closeDrawer(GravityCompat.START);
//            }
//        }, 200);
//    }
//
//    private void Exit_Fun() {
//        new AlertDialog.Builder(MainActivity.this).setMessage("Are You Sure You Want To Exit...?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finishAffinity();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }).show();
//    }
//
//    private void Logout() {
//        new AlertDialog.Builder(MainActivity.this).setMessage("Are You Sure You Want To Logout...?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(activity, "LogOut Successfully", Toast.LENGTH_SHORT).show();
//                        UserManager.logoutUser();
//                        getSharedPreferences("notice", Context.MODE_PRIVATE).edit().putBoolean("notice", false).apply();
//                        startActivity(new Intent(getApplicationContext(), Login.class));
//                        finish();
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }).show();
//    }
//
//    private void Share_App() {
//        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//        ClipData clip = ClipData.newPlainText("label", UserManager.getUserDetails(activity).getUserid());
//        clipboard.setPrimaryClip(clip);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String k = "";
//
//        k = "\n" +
//                "Refer with this code " + UserManager.getUserDetails(activity).getUserid() + "  and earn more." + " https://play.google.com/store/apps/details?id=" + getPackageName();
//
//        intent.putExtra(Intent.EXTRA_TEXT, k);
//        try {
//            startActivity(intent);
//        } catch (java.lang.Exception e) {
//        }
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
//
//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
//    }
//
//    public static void getUserDetails(Activity activity) {
//        String url = Utils.BASE_URL + "user/get_detail.php";
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                JSONObject jsonObject = null;
//                Log.e("user_data", "onResponse: " + response);
//                try {
//                    jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("status").equals("true")) {
//                        String ss = jsonObject.getJSONArray("details").toString();
//                        ArrayList<UserModel> list = new Gson().fromJson(ss, new TypeToken<ArrayList<UserModel>>() {
//                        }.getType());
//                        UserManager.setUserDetails(list.get(0), activity);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Utils.parseVolleyError(error, activity);
//            }
//        }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("userid", "" + UserManager.getUserDetails(activity).getUserid());
//                Log.e("get_details", "getParams: " + new Gson().toJson(hashMap));
//                return hashMap;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(activity);
//        requestQueue.getCache().clear();
//        requestQueue.add(stringRequest);
//    }

}