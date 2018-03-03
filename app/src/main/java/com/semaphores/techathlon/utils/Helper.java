package com.semaphores.techathlon.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Toast;


import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Helper {

    private static ACProgressFlower loading_dialogue;
    private static final String PREFS_NAME = "APP_PREFS" ;
    static SharedPreferences settings;


    /* Loading Box*/
    public static void init_loading_dialog(Context context){
        loading_dialogue =new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor( Color.CYAN )
                .sizeRatio(0.20f)
                .build();
    }
    public static void show_loading_dialog(){
        if(loading_dialogue!=null && !loading_dialogue.isShowing())
        loading_dialogue.show();
    }
    public static void hide_loading_dialog(){
        if(loading_dialogue!=null && loading_dialogue.isShowing())
            loading_dialogue.dismiss();
    }


//
//    /* Calligraphy */
//    public static void calligraphy_init() {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                                      .setDefaultFontPath("fonts/Titillium/Titillium-Light.otf")
//                                      .setFontAttrId(R.attr.fontPath)
//                                      .build()
//                                      );
//    }
//    public static Context calligraphy_wrap_context(Context context){
//        return CalligraphyContextWrapper.wrap(context);
//    }
//
//
//    /* Toast  */
//    public static void show_toast(Context context, int type , String message){
//        switch (type){
//            case 1: Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
//                    break;
//            case 2: Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show();
//                    break;
//            case 3: Toasty.info(context, message, Toast.LENGTH_SHORT, true).show();
//                    break;
//            case 4: Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
//                break;
//        }
//    }
//
//
//
//    /* Shared Prefs*/
//    public static double get_geoRadius(Context context){
//        settings = context.getSharedPreferences(PREFS_NAME, 0);
//        return ((double) settings.getInt("geo_radius", 0));
//    }
//    public static void save_geoRadius(Context context, int radius){
//        settings = context.getSharedPreferences(PREFS_NAME, 0);
//        settings.edit().putInt("geo_radius", radius).apply();
//    }




}
